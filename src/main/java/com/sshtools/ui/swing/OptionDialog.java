package com.sshtools.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import com.sshtools.ui.Option;
import com.sshtools.ui.OptionCallback;
import com.sshtools.ui.OptionChooser;

public class OptionDialog extends JOptionPane implements OptionChooser {

	private static final long serialVersionUID = 8490755354933812320L;

	public static boolean useDialogForPrompt = true;

	private boolean dismissed;
	private OptionCallback callback;
	private JDialog dialog;
	private boolean selectInitialValue = true;

	public OptionDialog(int type, Object text, Option choices[],
			Option defaultChoice, OptionCallback callback) {
		this(type, text, choices, defaultChoice, callback, null);
	}

	public OptionDialog(int type, Object text, Option choices[],
			Option defaultChoice, OptionCallback callback, Icon icon) {
		this(type, text, choices, defaultChoice, callback, null, null);
	}

	public OptionDialog(int type, Object text, Option choices[],
			Option defaultChoice, OptionCallback callback,
			Component buttonBarAccessory, Icon icon) {
		super("", type);

		if (type == PLAIN_MESSAGE) {
			setIcon(null);
		}

		dismissed = false;
		this.callback = callback;
		if (icon != null) {
			setIcon(icon);
		}

		setMessage(text);
		configureOptions(defaultChoice, choices, buttonBarAccessory, callback);
	}

	public void selectInitialValue() {
		if (selectInitialValue) {
			// If the message is a component and contains and other focusable
			// components, dont type and reset focus or initial value
			super.selectInitialValue();
		}
	}

	public void setValue(Object newValue) {
		if (callback != null) {
			Option optionForValue = getOptionForValue(newValue);
			if (optionForValue != null
					&& !callback.canClose(this, optionForValue)) {
				return;
			}
		}
		super.setValue(newValue);
	}

	private void configureOptions(Option defaultChoice, Option[] choices,
			Component buttonBarAccessory, OptionCallback callback) {
		if (buttonBarAccessory != null) {
			// If an accessory is provided we muse create our own buttons
			putClientProperty("OptionPane.buttonOrientation", new Integer(
					SwingConstants.RIGHT));
		} else {
			boolean opNo = false;
			boolean opYes = false;
			boolean opOk = false;
			boolean opCancel = false;
			boolean opDefault = false;
			boolean opOther = false;
			/*
			 * If we are dealing with standard options available in JOptionPane,
			 * then 'Option Type' will be used as this will use the current look
			 * and feel for the options buttons
			 */
			for (int i = 0; choices != null && i < choices.length; i++) {
				Option option = choices[i];
				if (option.equals(Option.CHOICE_NO)) {
					opNo = true;
				} else if (option.equals(Option.CHOICE_YES)) {
					opYes = true;
				} else if (option.equals(Option.CHOICE_CLOSE)) {
					opDefault = true;
				} else if (option.equals(Option.CHOICE_OK)) {
					opOk = true;
				} else if (option.equals(Option.CHOICE_CANCEL)) {
					opCancel = true;
				} else if (option.equals(Option.CHOICE_CANCEL)) {
					opOther = true;
					break;
				}
			}

			if (!opOther) {
				if (opNo && opYes && !opDefault && !opOk && !opCancel) {
					setOptionType(YES_NO_OPTION);
					return;
				} else if (opNo && opYes && opCancel && !opDefault && !opOk) {
					setOptionType(YES_NO_CANCEL_OPTION);
					return;
				} else if (opOk && opCancel && !opNo && !opYes && !opDefault) {
					setOptionType(OK_CANCEL_OPTION);
					return;
				} else if (opDefault && !opOk && !opCancel && !opNo && !opYes) {
					setOptionType(DEFAULT_OPTION);
					return;
				}
			}
		}

		// Create button components
		List<Component> choiceComponents = new ArrayList<Component>();
		if (buttonBarAccessory != null) {
			choiceComponents.add(buttonBarAccessory);
		}
		for (int i = 0; choices != null && i < choices.length; i++) {
			final JButton b = new JButton(choices[i].getText());
			if (choices[i] == defaultChoice) {
				b.setDefaultCapable(true);
				b.addHierarchyListener(new HierarchyListener() {
					public void hierarchyChanged(HierarchyEvent e) {
						if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
							JButton defaultButton = (JButton) e.getComponent();
							JRootPane root = SwingUtilities
									.getRootPane(defaultButton);
							if (root != null) {
								root.setDefaultButton(defaultButton);
							}
						}
					}
				});
			}
			b.setMnemonic(choices[i].getMnemonic());
			b.setToolTipText(choices[i].getText());
			b.setIcon(choices[i].getIcon());
			choiceComponents.add(b);
			b.putClientProperty("option", choices[i]);
			b.addActionListener(new BlockingActionListener(choices[i]));
		}
		setOptions(choiceComponents
				.toArray(new Object[choiceComponents.size()]));
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		OptionDialog.error(f,
				"Test", "This is a test error", new Exception("Test")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	public boolean isDismissed() {
		return dismissed;
	}

	public void choice(Option choice) {
		setValue(choice);
		dialog.dispose();
	}

	public Option dialogPrompt(Component parent, String title) {
		return dialogPrompt(parent, title, null);
	}

	public Option dialogPrompt(Component parent, String title, Dimension size) {
		return dialogPrompt(parent, title, null, -1, size);
	}

	public Option dialogPrompt(Component parent, String title,
			Image frameImage, int position) {
		return dialogPrompt(parent, title, frameImage, position, null);
	}

	public Option dialogPrompt(Component parent, String title,
			Image frameImage, int position, Dimension size) {
		dialog = createDialog(parent, title);
		if (frameImage != null) {
			UIUtil.setIconImage(dialog, frameImage);
		}
		if (position != -1) {
			UIUtil.positionComponent(position, dialog, parent);
		}
		if (size != null) {
			dialog.setSize(size);
		} else {
			dialog.pack();
		}
		dialog.setVisible(true);
		Object val = getValue();
		return getOptionForValue(val);
	}

	private Option getOptionForValue(Object val) {
		if (val == null) {
			return Option.CHOICE_CANCEL;
		}
		if (val instanceof Option) {
			return (Option) val;
		}
		if (val instanceof JButton) {
			return (Option) ((JButton) val).getClientProperty("option");
		}
		if (val instanceof Integer) {
			switch (((Integer) val).intValue()) {
			case YES_OPTION:
				// YES_OPTION is the same value as OK_OPTION
				if (getMessageType() == YES_NO_OPTION
						|| getMessageType() == YES_NO_CANCEL_OPTION) {
					return Option.CHOICE_YES;
				}
				return Option.CHOICE_OK;
			case NO_OPTION:
				return Option.CHOICE_NO;
			case CANCEL_OPTION:
				return Option.CHOICE_CANCEL;
			default:
				return Option.CHOICE_CLOSE;
			}
		}

		// String
		if (val instanceof Option) {
			return (Option) val;
		}

		// Frame closed?
		return null;
	}

	public static char[] promptForAuthentication(Component parent, String title) {
		return promptForAuthentication(parent, title,
				Messages.getString("OptionDialog.password")); //$NON-NLS-1$
	}

	public static char[] promptForAuthentication(Component parent,
			String title, String label) {
		String t = promptForText(parent, title, "", null, '*', label); //$NON-NLS-1$
		return t != null ? t.toCharArray() : null;
	}

	public static String promptForText(Component parent, String title,
			String defaultText, Component accessory, char echoCharacter,
			String label) {
		return promptForText(parent, title, defaultText, accessory,
				echoCharacter, label, -1, "South"); //$NON-NLS-1$
	}

	public static String promptForText(Component parent, String title,
			String defaultText, Component accessory, char echoCharacter,
			String label, int textWidth, String accesoryPosition) {
		JPanel p = new JPanel(new BorderLayout());
		JPanel middle = new JPanel(new FlowLayout());
		middle.add(new JLabel(label));
		JTextComponent textComponent;
		if (echoCharacter == ' ') {
			textComponent = new JPasswordField(defaultText,
					textWidth == -1 ? 15 : textWidth);
			((JPasswordField) textComponent).setEchoChar(echoCharacter);
		} else {
			textComponent = new XTextField(defaultText, textWidth == -1 ? 15
					: textWidth);
		}
		middle.add(textComponent);
		p.add(middle, BorderLayout.CENTER); //$NON-NLS-1$
		if (echoCharacter != ' ')
			textComponent.requestFocus();
		final OptionDialog dialog = new OptionDialog(QUESTION_MESSAGE, p,
				Option.CHOICES_OK_CANCEL, null, null);
		dialog.setSelectInitialValue(true);
		if (accessory != null)
			p.add(accessory, accesoryPosition);
		if (dialog.dialogPrompt(parent, title) == Option.CHOICE_OK)
			return textComponent.getText();
		return null;
	}

	private void setSelectInitialValue(boolean selectInitialValue) {
		this.selectInitialValue = selectInitialValue;
	}

	public static Option prompt(Component parent, int type, String title,
			Object text, Option choices[], Option defaultChoice) {
		return prompt(parent, type, title, text, choices, defaultChoice, null);
	}

	public static Option prompt(Component parent, int type, String title,
			Object text, Option choices[], Option defaultChoice,
			OptionCallback callback) {
		return prompt(parent, type, title, text, choices, defaultChoice,
				callback, null);
	}

	public static Option prompt(Component parent, int type, String title,
			Object text, Option choices[], Option defaultChoice,
			OptionCallback callback, Icon icon) {
		return prompt(parent, type, title, text, choices, defaultChoice,
				callback, icon, null);
	}

	public static Option prompt(Component parent, int type, String title,
			Object text, Option choices[], Option defaultChoice,
			OptionCallback callback, Icon icon, Dimension size) {
		return prompt(parent, type, title, text, choices, defaultChoice,
				callback, null, icon, defaultChoice != null, size);
	}

	public static Option prompt(Component parent, int type, String title,
			Object text, Option choices[], Option defaultChoice,
			OptionCallback callback, Component buttonBarAccesory, Icon icon,
			boolean selectInitialValue, Dimension size) {
		OptionDialog dialog = new OptionDialog(type, text, choices,
				defaultChoice, callback, buttonBarAccesory, icon);
		dialog.setSelectInitialValue(selectInitialValue);
		return dialog.dialogPrompt(parent, title, size);
	}

	/**
	 * Show an error message with detail
	 * 
	 * @param parent
	 * @param title
	 * @param exception
	 */
	public static void error(Component parent, String title, Throwable exception) {
		error(parent, title, null, exception);
	}

	/**
	 * Show an error message with detail
	 * 
	 * @param parent
	 * @param title
	 * @param exception
	 */
	public static void error(Component parent, String title, String message) {
		error(parent, title, message, null);
	}

	/**
	 * Show an error message with toggle-able detail
	 * 
	 * @param parent
	 * @param mesg
	 * @param title
	 * @param exception
	 */
	public static void error(Component parent, String title, String mesg,
			Throwable exception) {
		error(parent, title, mesg, exception, null, null);
	}

	/**
	 * Show an error message with toggle-able detail
	 * 
	 * @param parent
	 * @param mesg
	 * @param title
	 * @param exception
	 */
	public static Option error(Component parent, String title, String mesg,
			Throwable exception, Option[] options, Option defaultChoice) {
		boolean details = false;
		if (exception != null) {
			exception.printStackTrace();
		}
		while (true) {
			Vector optlist = new Vector();
			if (options != null) {
				for (int i = 0; i < options.length; i++) {
					optlist.addElement(options[i]);
				}
			}
			if (exception != null) {
				if (details) {
					optlist.addElement(Option.CHOICE_HIDE);
				} else {
					optlist.addElement(Option.CHOICE_SHOW);
				}
			}
			if (options == null) {
				optlist.addElement(Option.CHOICE_OK);
			}
			Option[] opts = new Option[optlist.size()];
			for (int i = 0; i < optlist.size(); i++) {
				opts[i] = (Option) optlist.elementAt(i);
			}

			StringBuffer buf = new StringBuffer();
			if (mesg != null) {
				buf.append(mesg + "\n"); //$NON-NLS-1$
			}
			appendException(exception, 0, buf, details);
			Component message;
			if (details) {
				MultilineLabel text = new MultilineLabel(buf.toString());
				message = new JScrollPane(
						JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				((JScrollPane) message).getViewport().setPreferredSize(
						new Dimension(300, 200));
				((JScrollPane) message).setViewportView(text);

			} else {
				message = new MultilineLabel(buf.toString());
			}
			Option opt = prompt(parent, OptionChooser.ERROR, title, message,
					opts, defaultChoice);
			if (opt == Option.CHOICE_HIDE || opt == Option.CHOICE_SHOW) {
				details = !details;
			} else {
				if (options != null) {
					return opt;
				}
				return null;
			}
		}
	}

	protected static void appendException(Throwable exception, int level,
			StringBuffer buf, boolean details) {
		try {
			if (((exception != null) && (exception.getMessage() != null))
					&& (exception.getMessage().length() > 0)) {
				if (details && (level > 0)) {
					buf.append("\n \nCaused by ...\n"); //$NON-NLS-1$
				}
				buf.append(exception.getMessage());
			}
			if (details) {
				if (exception != null) {
					if ((exception.getMessage() != null)
							&& (exception.getMessage().length() == 0)) {
						buf.append("\n \nCaused by ..."); //$NON-NLS-1$
					} else {
						buf.append("\n \n"); //$NON-NLS-1$
					}
				}
				StringWriter sw = new StringWriter();
				if (exception != null) {
					exception.printStackTrace(new PrintWriter(sw));
				}
				buf.append(sw.toString());
			}
			try {
				java.lang.reflect.Method method = exception.getClass()
						.getMethod("getCause", new Class[] {}); //$NON-NLS-1$
				Throwable cause = (Throwable) method.invoke(exception,
						(Object[]) null);
				if (cause != null) {
					appendException(cause, level + 1, buf, details);
				}
			} catch (Exception e) {
			}
		} catch (Throwable ex) {
		}
	}

	public static void info(Component parent, String title, String message) {
		prompt(parent, 0, title, message, Option.CHOICES_OK, Option.CHOICE_OK);
	}

	public Component getComponent() {
		return this;
	}

	class BlockingActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (callback != null
					&& !callback.canClose(OptionDialog.this, choice))
				return;
			setValue(choice);
			// dialog.dispose();
		}

		Option choice;

		BlockingActionListener(Option choice) {
			this.choice = choice;
		}
	}
}