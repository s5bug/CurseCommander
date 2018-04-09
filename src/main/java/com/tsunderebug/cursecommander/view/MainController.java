package com.tsunderebug.cursecommander.view;

import com.tsunderebug.cursecommander.model.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import static scala.collection.JavaConverters.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MainController {
	@FXML
	public ChoiceBox<String> versionSelector;
	@FXML
	private TextField searchField;
	@FXML
	private ListView<MainEntryController> mainList;
	@FXML
	private ListView<InstallationEntryController> selectedList;
	@FXML
	private WebView webView;
	@FXML
	private Button downloadButton;

	private Map<GameVersionID, Thread> downloadTasks = new HashMap<>();
	private Map<GameVersionID, Integer> page = new HashMap<>();
	private Map<GameVersionID, Set<ModSearchResult>> totalDownloaded = new HashMap<>();

	private Collection<GameVersionID> allVersions = asJavaCollection(GameVersionID$.MODULE$.all().toIterable());

	private GameVersionID selectedVersion = GameVersionID$.MODULE$.latestStable();

	private Set<ModSearchResult> selected = new HashSet<>();

	public MainController() {

	}

	public void switchToGameVersion(GameVersionID gv) {
		if(!totalDownloaded.containsKey(gv)) {
			totalDownloaded.put(gv, new HashSet<>());
		}
		if(downloadTasks.containsKey(selectedVersion)) {
			downloadTasks.get(selectedVersion).interrupt();
			downloadTasks.remove(selectedVersion);
		}
		updateEntryList(gv);
		selectedVersion = gv;
		createDownloadTask(selectedVersion);
	}

	public void updateEntryList(GameVersionID gv) {
		Set<ModSearchResult> l = totalDownloaded.getOrDefault(gv, new HashSet<>());
		List<MainEntryController> lmec = l.stream().sorted().filter(msr -> msr.name().toLowerCase().contains(searchField.getCharacters().toString().toLowerCase())).map(msr -> {
			MainEntryController m = new MainEntryController();
			m.setModNameText(msr.name());
			m.setModDescText(msr.smallDesc());
			m.setModIDText(msr.id());
			m.setMsr(msr);
			m.setOnMouseClicked(event -> {
				if(event.getButton() == MouseButton.PRIMARY) {
					addMod(msr);
				} else if (event.getButton() == MouseButton.SECONDARY) {
					removeMod(msr);
				}
				webView.getEngine().load(msr.link().toString());
			});
			return m;
		}).collect(Collectors.toList());
		Platform.runLater(() -> mainList.getItems().setAll(lmec));
	}

	public void createDownloadTask(GameVersionID gv) {
		Task<List<ModSearchResult>> task = new Task<List<ModSearchResult>>() {
			@Override
			protected List<ModSearchResult> call() {
				return Search.getAllSafeJ(gv, SortType.Name$.MODULE$, page.getOrDefault(gv, 1), l -> {
					totalDownloaded.getOrDefault(gv, new HashSet<>()).addAll(l);
					updateEntryList(gv);
					page.put(gv, page.getOrDefault(gv, 1) + 1);
				});
			}
		};
		Thread t = new Thread(task);
		downloadTasks.put(gv, t);
		t.start();
	}

	@FXML
	public void initialize() {
		downloadButton.setOnMouseClicked(event -> {
			downloadButton.setDisable(true);
			DirectoryChooser fileChooser = new DirectoryChooser();
			fileChooser.setTitle("Choose Download Directory");
			File downloadTo = fileChooser.showDialog(Main.instance);
			List<ModFile> downloadables = selectedList.getItems().stream().map(InstallationEntryController::getMsr).map(msr -> asJavaCollection(msr.files().toIterable()).stream().sorted((mf1, mf2) -> {
				String ml1 = mf1.link().toString();
				String ml2 = mf2.link().toString();
				return Integer.parseInt(ml2.substring(ml2.lastIndexOf('/') + 1)) - Integer.parseInt(ml1.substring(ml1.lastIndexOf('/') + 1));
			}).findFirst().get()).collect(Collectors.toList());
			new Thread(() -> {
				final AtomicInteger p = new AtomicInteger();
				downloadables.forEach(modFile -> {
					int pr = p.incrementAndGet();
					Platform.runLater(() -> downloadButton.setText("Downloading (" + pr + "/" + selected.size() + ")"));
					try {
						modFile.downloadTo(downloadTo);
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				Platform.runLater(() -> downloadButton.setDisable(false));
			}).start();
		});
		searchField.textProperty().addListener(((observable, oldValue, newValue) -> {
			updateEntryList(selectedVersion);
		}));
		versionSelector.getItems().setAll(allVersions.stream().map(GameVersionID::name).collect(Collectors.toList()));
		versionSelector.valueProperty().addListener((observable, oldValue, newValue) -> {
			GameVersionID nv = allVersions.stream().filter(fv -> fv.name().equals(newValue)).findFirst().get();
			switchToGameVersion(nv);
		});
		versionSelector.setValue(selectedVersion.name());
	}

	public void addMod(ModSearchResult msr) {
		addModSub(msr);
		Platform.runLater(() -> selectedList.getItems().setAll(selectedEntryControllers()));
	}

	public List<InstallationEntryController> selectedEntryControllers() {
		return selected.stream().map((msr) -> {
			InstallationEntryController iec = new InstallationEntryController();
			iec.setMc(this);
			iec.setMsr(msr);
			iec.setModNameText(msr.name());
			return iec;
		}).collect(Collectors.toList());
	}

	public void addModSub(ModSearchResult msr) {
		Platform.runLater(() -> {
			selected.add(msr);
			asJavaCollection(msr.dependencies().toIterable()).forEach(i -> {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				addModSub(i);
			});
		});
	}

	public void removeMod(ModSearchResult msr) {
		removeModSub(msr);
		Platform.runLater(() -> selectedList.getItems().setAll(selectedEntryControllers()));
	}

	public void removeModSub(ModSearchResult msr) {
		Platform.runLater(() -> {
			selected.remove(msr);
			asJavaCollection(msr.dependents().toIterable()).forEach(i -> {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				removeModSub(i);
			});
		});
	}

}
