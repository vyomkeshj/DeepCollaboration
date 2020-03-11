/*
 * Copyright (c) 2010, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.vsb.realCollaborationn.visualization.renderer;

import edu.vsb.realCollaborationn.learning.URAgent;
import edu.vsb.realCollaborationn.visualization.importers.Importer3D;
import edu.vsb.realCollaborationn.visualization.importers.Optimizer;
import edu.vsb.realCollaborationn.visualization.robot.UR3Model;
import edu.vsb.realCollaborationn.visualization.shape3d.model.ArtifactStructure;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller class for main fxml file.
 */
public class MainController implements Initializable {
    public SplitMenuButton openMenuBtn;
    public Label status;
    public SplitPane splitPane;
    public ToggleButton settingsBtn;
    public CheckMenuItem loadAsPolygonsCheckBox;
    public CheckMenuItem optimizeCheckBox;
    public Button startBtn;
    public Button rwBtn;
    public ToggleButton playBtn;
    public Button ffBtn;
    public Button endBtn;
    public ToggleButton loopBtn;
    public TimelineDisplay timelineDisplay;
    private Accordion settingsPanel;
    private double settingsLastWidth = -1;
    private int nodeCount = 0;
    private int meshCount = 0;
    private int triangleCount = 0;
    private final ContentModel contentModel = RealCollaborationApplication.getContentModel();
    private File loadedPath;
    private String loadedURL;
    private String[] supportedFormatRegex;
    private SessionManager sessionManager = SessionManager.getSessionManager();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // CREATE NAVIGATOR CONTROLS
            Parent navigationPanel  = FXMLLoader.load(getClass().getResource("/navigation.fxml"));
            // CREATE SETTINGS PANEL
            settingsPanel = FXMLLoader.load(getClass().getResource("/settings.fxml"));
            // SETUP SPLIT PANE
            splitPane.getItems().addAll(new SubSceneResizer(contentModel.subSceneProperty(),navigationPanel), settingsPanel);
            splitPane.getDividers().get(0).setPosition(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // listen for drops
        supportedFormatRegex = Importer3D.getSupportedFormatExtensionFilters();
        for (int i=0; i< supportedFormatRegex.length; i++) {
            supportedFormatRegex[i] = "."+supportedFormatRegex[i].replaceAll("\\.","\\.");
//            System.out.println("supportedFormatRegex[i] = " + supportedFormatRegex[i]);
        }
        // do initial status update
        ArtifactStructure model = URAgent.robotModel;

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URAgent.urAgent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        contentModel.setContent(model.getStructureNode());
        updateStatus();
    }

    public void open(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Supported files", Importer3D.getSupportedFormatExtensionFilters()));
        if (loadedPath != null) {
            chooser.setInitialDirectory(loadedPath.getAbsoluteFile().getParentFile());
        }
        chooser.setTitle("Select file to load");
        File newFile = chooser.showOpenDialog(openMenuBtn.getScene().getWindow());
        if (newFile != null) {
            load(newFile);
        }
    }

    private void load(File file) {
        loadedPath = file;
        try {
            doLoad(file.toURI().toURL().toString());
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void load(String fileUrl) {
        try {
            try {
                loadedPath = new File(new URL(fileUrl).toURI()).getAbsoluteFile();
            } catch (IllegalArgumentException | MalformedURLException | URISyntaxException ignored) {
                loadedPath = null;
            }
            doLoad(fileUrl);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void doLoad(String fileUrl) {
        loadedURL = fileUrl;
        sessionManager.getProperties().setProperty(RealCollaborationApplication.FILE_URL_PROPERTY, fileUrl);
        try {
            Node content = Importer3D.loadModelFile(
                    fileUrl, loadAsPolygonsCheckBox.isSelected());

            if (optimizeCheckBox.isSelected()) {
                new Optimizer(content, true).optimize();
            }
            contentModel.setContent(content);   //fixme: setContent, is it fair?


        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateStatus();
    }

    private void updateStatus() {
        nodeCount = 0;
        meshCount = 0;
        triangleCount = 0;
        updateCount(contentModel.getRoot3D());
        Node content = contentModel.getContent();
        final Bounds bounds = content == null ? new BoundingBox(0, 0, 0, 0) : content.getBoundsInLocal();
        status.setText(
                String.format("Nodes [%d] :: Meshes [%d] :: Triangles [%d] :: " +
                               "Bounds [w=%.2f,h=%.2f,d=%.2f]",
                              nodeCount,meshCount,triangleCount,
                              bounds.getWidth(),bounds.getHeight(),bounds.getDepth()));
    }

    private void updateCount(Node node){
        nodeCount ++;
        if (node instanceof Parent) {
            for(Node child: ((Parent)node).getChildrenUnmodifiable()) {
                updateCount(child);
            }
        } else if (node instanceof Box) {
            meshCount ++;
            triangleCount += 6*2;
        } else if (node instanceof MeshView) {
            TriangleMesh mesh = (TriangleMesh)((MeshView)node).getMesh();
            if (mesh != null) {
                meshCount ++;
                triangleCount += mesh.getFaces().size() / mesh.getFaceElementSize();
            }
        }
    }

    public void toggleSettings(ActionEvent event) {
        final SplitPane.Divider divider = splitPane.getDividers().get(0);
        if (settingsBtn.isSelected()) {
            if (settingsLastWidth == -1) {
                settingsLastWidth = settingsPanel.prefWidth(-1);
            }
            final double divPos = 1 - (settingsLastWidth / splitPane.getWidth());

        } else {
            settingsLastWidth = settingsPanel.getWidth();
            settingsPanel.setMinWidth(0);
        }
    }
}
