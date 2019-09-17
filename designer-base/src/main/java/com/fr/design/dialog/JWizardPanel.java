package com.fr.design.dialog;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.fr.design.layout.FRGUIPaneFactory;

/**
 * WizardPane. Add with getContentPane().
 */
public class JWizardPanel extends BasicPane {
	JWizardDialog dialogParent;

	// The step title
	JPanel titlePanel;
	String stepTitle;
	UILabel stepTitleLabel;

	// The content pane
	JPanel contentPane;

	// The back and next steps
	int backStep = -1;
	int nextStep = -1;

	// Flags the first time the component is added to a window
	boolean firstNotify = true;

	/**
	 * Creates a new JWizardPanel with a double buffer and a flow layout. The
	 * flow layout is assigned to the panel accessed through getContentPane().
	 */
	public JWizardPanel() {
		super();
		initComponents();
	}

	/**
	 * Initialize the JWizardPanel.
	 */
	private void initComponents() {
		setLayout(FRGUIPaneFactory.createBorderLayout());

		// Set the layout for the content area
		contentPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();

		contentPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 4, 4));

		// Step title
		titlePanel =FRGUIPaneFactory.createBorderLayout_S_Pane();
		titlePanel.add(new JSeparator(), BorderLayout.SOUTH);
		stepTitleLabel = new UILabel(" ");
		titlePanel.add(stepTitleLabel);

		add(titlePanel, BorderLayout.NORTH);
		add(contentPane, BorderLayout.CENTER);
	}

	@Override
	protected String title4PopupWindow() {
		return "wizard";
	}

	/**
	 * Set the title to use for this step. Normally this title would be unique
	 * for each wizards step.
	 *
	 * @param stepTitle The title to use for this step.
	 */
	public void setStepTitle(String stepTitle) {
		this.stepTitle = stepTitle;
		stepTitleLabel.setText(stepTitle);

		stepTitleLabel.invalidate();
		validate();
	}

	/**
	 * Get the step title to use for this step.
	 *
	 * @return The step title to use for this step.
	 */
	public String getStepTitle() {
		return stepTitle;
	}

	/**
	 * Get a JPanel to use for adding your own components to this WizardPanel.
	 * Do not add components directly to the JWizardPanel. The JPanel uses the
	 * layout given in the JWizardPanel constructor.
	 *
	 * @return The JPanel to use for adding components for this wizard step.
	 */
	public JPanel getContentPane() {
		return contentPane;
	}

	/**
	 * Get the wizard step to go to when the Back button is pressed.
	 *
	 * @return The wizard step to go to when the Back button is pressed.
	 */

	public int getBackStep() {
		return backStep;
	}

	/**
	 * Set the wizard step to go to when the Back button is pressed. This should
	 * be set in the constructor of the JWizardPanel subclass since it
	 * determines whether the Back button is enabled or not.
	 *
	 * @param backStep
	 *            The wizard step to go to when the Back button is pressed.
	 */

	public void setBackStep(int backStep) {
		this.backStep = backStep;
		JWizardDialog dialog = getWizardParent();
		if (dialog != null) {
			dialog.applyButtonStates();
		}
	}

	/**
	 * Get the wizard step to go to when the Next button is pressed.
	 *
	 * @return The wizard step to go to when the Next button is pressed.
	 */
	public int getNextStep() {
		return nextStep;
	}

	/**
	 * Set the wizard step to go to when the Next button is pressed. This should
	 * be set in the constructor of the JWizardPanel subclass since it
	 * determines whether the Next and Finish buttons are enabled or not.
	 *
	 * @param nextStep The wizard step to go to when the Next button is pressed.
	 */
	public void setNextStep(int nextStep) {
		this.nextStep = nextStep;
		JWizardDialog dialog = getWizardParent();
		if (dialog != null) {
			dialog.applyButtonStates();
		}
	}

	/**
	 * Returns the JWizardDialog in which this JWizardPanel resides. This is
	 * valid only after the panel has been added to the dialog.
	 *
	 * @return The JWizardDialog in which this JWizardPanel resides.
	 */

	public JWizardDialog getWizardParent() {
		return dialogParent;
	}

	/**
	 * Do not call directly.
	 */
	public void addNotify() {
		if (firstNotify) {
			Font font = stepTitleLabel.getFont();
			font = font.deriveFont(Font.BOLD, font.getSize() * 14 / 10.0F);
			stepTitleLabel.setFont(font);
			firstNotify = false;
		}
		super.addNotify();
	}

	/**
	 * Set the JWizardDialog parent for this JWizardPanel.
	 *
	 * @param dialogParent
	 *            The JWizardPanel parent for this JWizardPanel.
	 */
	void setWizardParent(JWizardDialog dialogParent) {
		this.dialogParent = dialogParent;
	}

	/**
	 * Calls back(). This allows the JWizardDialog to call the protected method
	 * back().
	 *
	 * @see #back()
	 */

	void doBack() {
		back();
	}

	/**
	 * Calls next(). This allows the JWizardDialog to call the protected method
	 * next().
	 *
	 * @see #next()
	 */
	void doNext() {
		next();
	}

	/**
	 * Called when the Back button is pressed. By default this displays the
	 * wizard step set by setBackStep().
	 *
	 * @see #setBackStep(int)
	 */
	protected void back() {
		dialogParent.goTo(getBackStep());
	}

	/**
	 * Called when the Next button is pressed. By default this displays the
	 * wizard step set by setNextStep().
	 *
	 * @see #setNextStep(int)
	 */
	protected void next() {
		dialogParent.goTo(getNextStep());
	}

}
