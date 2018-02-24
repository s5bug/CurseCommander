package com.tsunderebug.cursecommander.view;

import com.tsunderebug.cursecommander.model.ModSearchResult;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class InstallationEntryController extends HBox {
	@FXML
	private CheckBox modName;

	private MainController mc;

	private ModSearchResult msr;

	public InstallationEntryController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/installationentry.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
		modName.selectedProperty().addListener((observable, oldValue, newValue) -> mc.removeMod(msr));
	}

	public void setModNameText(String s) {
		modName.setText(s);
	}

	public String getModNameText() {
		return modName.getText();
	}

	public StringProperty modNameTextProperty() {
		return modName.textProperty();
	}

	public void setMc(MainController mc) {
		this.mc = mc;
	}

	public MainController getMc() {
		return this.mc;
	}

	public void setMsr(ModSearchResult msr) {
		this.msr = msr;
	}

	public ModSearchResult getMsr() {
		return this.msr;
	}

}
