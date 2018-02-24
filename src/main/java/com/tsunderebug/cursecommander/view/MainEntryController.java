package com.tsunderebug.cursecommander.view;

import com.tsunderebug.cursecommander.model.ModSearchResult;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import java.io.IOException;

public class MainEntryController extends BorderPane {
	@FXML
	private Text modName;
	@FXML
	private Text modDesc;
	@FXML
	private Text modID;

	private ModSearchResult msr;

	public MainEntryController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/mainentry.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
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

	public void setModDescText(String s) {
		modDesc.setText(s);
	}

	public String getModDescText() {
		return modDesc.getText();
	}

	public StringProperty modDescTextProperty() {
		return modDesc.textProperty();
	}

	public void setModIDText(String s) {
		modID.setText(s);
	}

	public String getModIDText() {
		return modID.getText();
	}

	public StringProperty modIDTextProperty() {
		return modID.textProperty();
	}

	public void setMsr(ModSearchResult msr) {
		this.msr = msr;
	}

	public ModSearchResult getMsr() {
		return this.msr;
	}

}
