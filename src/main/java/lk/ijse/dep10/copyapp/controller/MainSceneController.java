package lk.ijse.dep10.copyapp.controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lk.ijse.dep10.copyapp.AppInitializer;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainSceneController {

    @FXML
    private Button btnCopy;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnMove;

    @FXML
    private Button btnPutBrowse;

    @FXML
    private Button btnSelectBrowse;

    @FXML
    private ProgressBar pgbCopyMove;

    @FXML
    private TextField txtPutPath;

    @FXML
    private TextField txtSelectPath;

    private ArrayList<File> arrayListFile;
    private ArrayList<File> arrayListDirectory;


    private ArrayList<File> replaceArrayList;
    private ArrayList<File> replaceFile;

    private File selectPath;
    private  File putPath;
    private int count=0;


    public void initialize(){

    }

    @FXML
    void btnCopyOnAction(ActionEvent event) {
        btnCopy.getScene().getWindow().setHeight(300);
        if (selectPath.isDirectory()) {
            String parentName = selectPath.getName();
            File parentFile = new File(putPath, parentName);
            parentFile.mkdir();
            if (replaceArrayList.isEmpty()) {
                copyFile();
            }else{
                for (int i = 0; i < replaceArrayList.size(); i++) {
                    File file=new File(replaceArrayList.get(i).toString());
                    file.mkdirs();
                }
                copyFile();
            }
        }else{
            copyFile();
        }

        Alert alert=new Alert(Alert.AlertType.INFORMATION,"Completed the copying");
        alert.show();
        txtSelectPath.clear();
        txtPutPath.clear();
    }


    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        btnCopy.getScene().getWindow().setHeight(300);
        for (File fileDelete : arrayListFile) {
            fileDelete.delete();
        }
        for (int i=arrayListDirectory.size()-1;i>=0;i--) {
            arrayListDirectory.get(i).delete();
        }
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"Completed the Deleting");
        alert.show();
        selectPath.delete();
        txtSelectPath.clear();

    }

    @FXML
    void btnMoveOnAction(ActionEvent event) throws InterruptedException {
        btnCopy.getScene().getWindow().setHeight(300);
        btnCopy.fire();
        btnDelete.fire();
        Alert alert=new Alert(Alert.AlertType.INFORMATION,"Completed the Moving");
        alert.show();


    }

    @FXML
    void btnPutBrowseOnAction(ActionEvent event) {
        btnCopy.getScene().getWindow().setHeight(210);
        replaceArrayList=new ArrayList<>();
        replaceFile=new ArrayList<>();
        DirectoryChooser destination=new DirectoryChooser();
        destination.setTitle("Select a folder");
        putPath=destination.showDialog(btnPutBrowse.getScene().getWindow());
        if(putPath==null)return;
        txtPutPath.setText(putPath.getAbsolutePath());
        enableButtons();

        replacePathDestination(arrayListDirectory);
        replaceFile(arrayListFile);
    }

    @FXML
    void btnSelectBrowseOnAction(ActionEvent event) {
        btnCopy.getScene().getWindow().setHeight(210);
        arrayListDirectory=new ArrayList<>();
        arrayListFile=new ArrayList<>();
        JFileChooser chooser=new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showOpenDialog(null);
        selectPath=chooser.getSelectedFile();
        if(selectPath==null)return;
        txtSelectPath.setText(chooser.getSelectedFile()+"");
        findFoldersAndFiles(selectPath);
        enableButtons();


    }

    private void findFoldersAndFiles(File file){
        if(!file.isDirectory()){
            txtSelectPath.setText(file.getAbsolutePath());
            arrayListFile.add(file);
            return;
        }
        File[] fileFind=file.listFiles();
            for (File fileOrDirectory : fileFind) {
                if (fileOrDirectory.isDirectory()) {
                    arrayListDirectory.add(fileOrDirectory);
                    findFoldersAndFiles(fileOrDirectory);
                } else {
                    arrayListFile.add(fileOrDirectory);
                }
            }
    }

    private void replacePathDestination(ArrayList<File> arrayList){
        String parentPath=selectPath.getParentFile().toString();
        String destination=putPath.getPath();
        int index=parentPath.length();
        for (File path : arrayList) {
            String name=path.toString();
            String repalcePath=destination+name.substring(index);
            File test=new File(repalcePath);
            replaceArrayList.add(test);
        }
    }

    private void replaceFile(ArrayList<File> arrayList){
        String parentPath=selectPath.getParentFile().toString();
        String destination=putPath.getPath();
        int index=parentPath.length();
        for (File file : arrayList) {
            String name=file.toString();
            String repalcePath=destination+name.substring(index);
            File test=new File(repalcePath);
            replaceFile.add(test);
        }
    }

    private void enableButtons(){
        btnCopy.setDisable(selectPath==null || putPath==null || selectPath.getParentFile().equals(putPath) || selectPath.equals(putPath));
        btnMove.setDisable(selectPath==null || putPath==null || selectPath.getParentFile().equals(putPath) || selectPath.equals(putPath));
        btnDelete.setDisable(selectPath==null);
    }

    private void copyFile(){
        count=0;
        for (int i = 0; i < arrayListFile.size(); i++) {
            System.out.println("a");
            try {
                FileInputStream fis = new FileInputStream(arrayListFile.get(i));
                FileOutputStream fos = new FileOutputStream(replaceFile.get(i));
                while (true) {
                    byte[] buffer = new byte[1024 * 10];
                    int read = fis.read(buffer);
                    if (read == -1) break;
                    fos.write(buffer, 0, read);
                }
                fis.close();
                fos.close();
                count++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
