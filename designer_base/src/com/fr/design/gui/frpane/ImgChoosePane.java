package com.fr.design.gui.frpane;

import com.fr.base.BaseUtils;
import com.fr.base.background.ImageBackground;
import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.style.background.image.ImageFileChooser;
import com.fr.design.style.background.image.ImagePreviewPane;
import com.fr.design.utils.ImageUtils;
import com.fr.general.Inter;
import com.fr.stable.CoreGraphHelper;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ImgChoosePane extends BasicPane {

	private ImagePreviewPane previewPane;
	private UIButton chooseButton;
	private UIButton clearButton;
	private UILabel imgSizeLabel;
	private ImageFileChooser imageFileChooser;
	private SwingWorker<Void, Void> imageWorker;

	public ImgChoosePane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());

		JPanel previewContainner = new JPanel();
		previewContainner.setBorder(BorderFactory.createTitledBorder(Inter.getLocText("Preview")));

		previewPane = new ImagePreviewPane();
		previewContainner.add(previewPane);
		previewPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent evt) {
				Image image = ((ImagePreviewPane) evt.getSource()).getImage();

				if (image == null) {
					imgSizeLabel.setText("");
				} else {
//					imgSizeLabel.setText(Inter.getLocText("Size") + ": " + image.getWidth(null) + "px"
//							+ image.getHeight(null) + Inter.getLocText("px"));
					imgSizeLabel.setText(Inter.getLocText(new String[]{"Size", "px", "px"},
							new String[]{": " + image.getWidth(null), image.getHeight(null) + ""}));
				}
			}
		});

		this.add(previewContainner, BorderLayout.CENTER);

		JPanel choosePane = new JPanel(new BorderLayout(0, 10));
		choosePane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		JPanel choosePane1 = new JPanel(new BorderLayout(0, 10));

		initButton();
		choosePane.add(chooseButton, BorderLayout.NORTH);

		choosePane1.add(clearButton,BorderLayout.NORTH);
		choosePane.add(choosePane1,BorderLayout.CENTER);
		
		imgSizeLabel = new UILabel();
		imgSizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		choosePane1.add(imgSizeLabel,BorderLayout.CENTER);
		this.add(choosePane,BorderLayout.EAST);

		imageFileChooser = new ImageFileChooser();
	}

	private void initButton() {
		chooseButton = new UIButton(Inter.getLocText("Image-Select_Picture"));
		chooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = imageFileChooser.showOpenDialog(ImgChoosePane.this);
				if (returnVal != JFileChooser.CANCEL_OPTION) {
					File selectedFile = imageFileChooser.getSelectedFile();
					if (selectedFile != null && selectedFile.isFile()) {
						previewPane.showLoading();
						if (imageWorker != null && !imageWorker.isDone()) {
							imageWorker = null;
						}
						imageWorker = new SwingWorker<Void, Void>() {
							@Override
							protected Void doInBackground() throws Exception {
								Image image = imageFileChooser.isCompressSelected() ? ImageUtils.defaultImageCompress(selectedFile) : BaseUtils.readImage(selectedFile.getPath());
								CoreGraphHelper.waitForImage(image);

								previewPane.setImage(image);
								previewPane.repaint();
								return null;
							}
						};
						imageWorker.execute();
					} else {
						previewPane.setImage(null);
					}
				}
			}
		});

		clearButton = new UIButton(Inter.getLocText("Clear"));
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				previewPane.setImage(null);
				previewPane.repaint();
			}
		});
	}
	
	public void populate(ImageBackground background) {
		if(background != null && background.getImage() != null) {
			previewPane.setImage(background.getImage());
		}
	}
	
	public ImageBackground update() {
		if(previewPane.getImage() == null) {
			return null;
		}
		return new ImageBackground(previewPane.getImage());
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("Image");
	}
}