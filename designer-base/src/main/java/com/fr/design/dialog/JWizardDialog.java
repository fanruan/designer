package com.fr.design.dialog;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Wizard dialog.
 */
public class JWizardDialog extends BasicDialog {
	//The label which holds the image to display on the left side of the wizard.
	private JPanel logoPanel;
	private UILabel logoLabel;

	//The panel to which JWizardPanel's are added.
	private JPanel workArea;

	//The layout for the work area.
	private CardLayout cardLayout;

	//The current JWizardPanel
	private JWizardPanel currentWizard = null;

	private int currentStep = -1;
	private int lastStep = -1;
	private int panelCount = 0;

	//The buttons
	private UIButton buttonBack;
	private UIButton buttonNext;
	private UIButton buttonFinish;
	private UIButton buttonCancel;

	// True if the finish button should be enabled all the time
	private boolean enableEarlyFinish = false;

	// True if the cancel button is enabled on the final step
	private boolean enableCancelAtEnd = true;
	
    /**
     * Shows dialog
     */
    public static JWizardDialog showWindow(Window window) {
        if (window instanceof Frame) {
            return new JWizardDialog((Frame) window);
        } else {
            return new JWizardDialog((Dialog) window);
        }
    }	

    protected JWizardDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	protected JWizardDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	/**
	 * Initialize the JWizardDialog.
	 */
	protected void initComponents() {
		JPanel contentPane = (JPanel)this.getContentPane();
		contentPane.setLayout(FRGUIPaneFactory.createBorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.applyClosingAction();
		this.applyEscapeAction();
		// If the user tries to close the wizard, the result should be the
		// same as pressing Cancel
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		// Window close is the same as cancel. If the cancel button is
		// disabled, then a window close does nothing
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (buttonCancel.isEnabled()) {
					cancel();
				}
			}
		});

		// Work area for WizardPanel's
		workArea = FRGUIPaneFactory.createCardLayout_S_Pane();
		cardLayout = new CardLayout();
		workArea.setLayout(cardLayout);

		// Buttons
		buttonBack = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Previous"));
		buttonBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				back();
			}
		});
		buttonNext = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Next"));
		buttonNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				next();
			}
		});
		buttonFinish = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Finish"));
		buttonFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				finish();
			}
		});
		buttonCancel = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Cancel"));
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cancel();
			}
		});
		JPanel buttons = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
		
		//p:几个按钮必须要等距,所以才需要用GridLayout.
		JPanel bbPane = FRGUIPaneFactory.createMediumHGapFlowInnerContainer_M_Pane();
		buttons.add(bbPane);	
		bbPane.add(buttonBack);
		bbPane.add(buttonNext);
		bbPane.add(buttonFinish);
		bbPane.add(buttonCancel);

		JPanel buttonPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
		buttonPanel.add(new JSeparator(), BorderLayout.NORTH);
		buttonPanel.add(buttons);

		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		contentPane.add(workArea);
		
		this.setModal(true);
	}
	
	/**
	 * Add an image which displays on the left side of the wizard. By
	 * default, no image is displayed. This must be set before the dialog
	 * is made visible.
	 *
	 * @param icon The icon representing the image to display. If null, no
	 * 	image is displayed.
	 */
	public void setWizardIcon(Icon icon) {
		// If null, remove any existing logo panel
		if (icon == null) {
			if (logoPanel != null) {
				remove(logoPanel);
				logoPanel = null;
				logoLabel = null;
			}
		}
		//If not null, add it or replace an existing label
		else {
			if (logoPanel != null) {
				remove(logoPanel);
			}
			logoPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
			logoLabel = new UILabel(icon);
			logoPanel.add(logoLabel, BorderLayout.NORTH);
			getContentPane().add(logoPanel, BorderLayout.WEST);
		}
	}

	/**
	 * Add a panel representing a step in the wizard. Since removing a
	 * panel would force a renumbering of the remaining panels and since
	 * you have flexible sequencing control, there is no matching
	 * removeWizardPanel() method.
	 *
	 * @param panel The JWizardPanel to add
	 */

	public void addWizardPanel(JWizardPanel panel) {
		if (currentWizard == null) {
			currentWizard = panel;
			currentStep = 0;
		}
		workArea.add(panel, Integer.toString(panelCount++));
		panel.setWizardParent(this);
	}

	/**
	 * If this method is called, the Finish button is enabled immediately.
	 * By default, it is enabled only on the last step (any step where the
	 * next JWizardPanel step is -1).
	 */

	public void setEarlyFinish() {
		enableEarlyFinish = true;
	}

	/**
	 * If this method is called, the Cancel button is disabled when on the
	 * last step. If setEarlyFinish() is called, it is still disabled only
	 * on the last step.
	 */

	public void disableCancelAtEnd() {
		enableCancelAtEnd = false;
	}

	/**
	 * Returns the current step being displayed by the wizard. Steps start
	 * at 0. If no step is yet displayed, a -1 is returned.
	 *
	 * @return The current step being displayed by the wizard.
	 */

	public int getCurrentStep() {
		return currentStep;
	}

	/**
	 * Returns the last step displayed by the wizard. Steps start at 0. If
	 * there is no previous step yet, -1 is returned.
	 *
	 * @return The last step being displayed by the wizard.
	 */

	public int getLastStep() {
		return lastStep;
	}

	/**
	 * Set the sensitivity of each button based on the back and next step
	 * values. This should be called when changing steps or when the back
	 * or next button values are changed.
	 */
	public void applyButtonStates() {
		int backStep = currentWizard.getBackStep();
		int nextStep = currentWizard.getNextStep();

		boolean atBegin = backStep < 0 || backStep >= panelCount;

		boolean atEnd = nextStep < 0 || nextStep >= panelCount;

		buttonBack.setEnabled(!atBegin);
		buttonNext.setEnabled(!atEnd);
		buttonFinish.setEnabled(enableEarlyFinish || atEnd);
		buttonCancel.setEnabled(!atEnd || enableCancelAtEnd);

		// Set the default button
		if (buttonNext.isEnabled()) {
			getRootPane().setDefaultButton(buttonNext);
		} else if (buttonFinish.isEnabled()) {
			getRootPane().setDefaultButton(buttonFinish);
		} else if (buttonBack.isEnabled()) {
			getRootPane().setDefaultButton(buttonBack);
		} else {
			getRootPane().setDefaultButton(null);
		}
	}

	/**
	 * Display the JWizardPanel with the given step number. This method is
	 * package public so that JWizardPanel can call it. The
	 * switchToStep() method may override the step choice.
	 * </code></pre>
	 *
	 * @param step The step number of the JWizardPanel to display.
	 * @see #switchToStep(int,int)
	 */
	protected void goTo(int step) {
		// Give the user a last chance to change things
		step = switchToStep(currentStep, step);

		// We can't do anything if we're outside the valid range
		if (step < 0 || step >= panelCount) {
			return;
		}

		// Save the current step as the previous step
		lastStep = currentStep;

		currentWizard = (JWizardPanel) workArea.getComponent(step);
		currentStep = step;
		cardLayout.show(workArea, Integer.toString(step));

		// Set the button states
		applyButtonStates();
	}

	//**********************************************************************
	// Protected
	//**********************************************************************

	/**
	 * Called when the Back button is pressed. This calls the back()
	 * method in the current JWizardPanel.
	 *
	 * @see JWizardPanel#back()
	 */
	protected void back() {
		if (currentWizard != null) {
			currentWizard.doBack();
		}
	}

	/**
	 * Called when the Next button is pressed. This calls the next()
	 * method in the current JWizardPanel.
	 *
	 * @see JWizardPanel#next()
	 */
	protected void next() {
		if (currentWizard != null) {
			currentWizard.doNext();
		}
	}

	/**
	 * Called when the Finish button is pressed. This calls dispose(). You
	 * will probably want to override this.
	 */
	protected void finish() {
		doOK();
	}

	/**
	 * Called when the Cancel button is pressed. This calls dispose().
	 */
	protected void cancel() {
		doCancel();
	}

	/**
	 * This method is called just prior to switching from one step to
	 * another (after any next() or back() method is called). It receives
	 * the current and new indices. By default, it returns the new index.
	 * You can override the method if you need to control sequencing from
	 * this JWizardDialog class (normally, each step decides what the back
	 * and next steps should be).
	 *
	 * @param currentIndex The index of the current JWizardPanel.
	 * @param newIndex The index of the JWizardPanel we are about to display.
	 * @return The index of the JWizardPanel to display.
	 */
	protected int switchToStep(int currentIndex, int newIndex) {
		return newIndex;
	}
	
	/**
	 * Get the currentWizard Pane
	 */
	protected JWizardPanel getCurrentWizard() {
		return this.currentWizard;
	}
	
	@Override
	public void checkValid() throws Exception {
		//do nothing
	}
}
