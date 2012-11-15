package pl.uz.zgora.calculator.ui;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import pl.uz.zgora.calculator.asm.AsmGenerator;

public class MainWorkbench {

	protected Shell shlCalc;
	private Text result;
	private Text command;
	private UiControler controler;
	private Button evaluate;
	private Button clear;
	private TreeDrawer visualTree;
	private AsmEditor asemblerCodeText;

	public MainWorkbench(UiControler prog) {
		this.controler = prog;
	}

	public MainWorkbench() {
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MainWorkbench window = new MainWorkbench();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlCalc.open();
		shlCalc.layout();
		while (!shlCalc.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlCalc = new Shell();
		shlCalc.setSize(569, 464);
		shlCalc.setText("Calc by Wojciech Trocki");
		shlCalc.setLayout(new GridLayout(2, false));

		Group grpCommand = new Group(shlCalc, SWT.NONE);
		grpCommand.setText("Command");
		grpCommand.setLayout(new GridLayout(2, false));
		GridData gd_grpCommand = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_grpCommand.widthHint = 296;
		gd_grpCommand.heightHint = 73;
		grpCommand.setLayoutData(gd_grpCommand);

		command = new Text(grpCommand, SWT.BORDER | SWT.MULTI);
		command.setBackground(SWTResourceManager.getColor(255, 255, 255));
		command.setText("15*4+7");
		command.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		GridData gd_command = new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1);
		gd_command.widthHint = 172;
		gd_command.heightHint = 51;
		command.setLayoutData(gd_command);

		evaluate = new Button(grpCommand, SWT.NONE);
		GridData gd_evaluate = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				1);
		gd_evaluate.widthHint = 76;
		gd_evaluate.heightHint = 54;
		evaluate.setLayoutData(gd_evaluate);
		evaluate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = command.getText();
				double resultVal;
				try {
					visualTree.getDrawerTree().removeAll();
					resultVal = controler.calc(text);
					result.setText(" " + resultVal);
					visualTree.setTree(controler.getTree());
					visualTree.setSymbols(controler.getSymbols());
					visualTree.computeText();

				} catch (Exception e2) {
					MessageBox mb = new MessageBox(shlCalc);
					mb.setMessage("Invalid input");
					mb.setText("Parser error");
					mb.open();
					e2.printStackTrace();
				}
				try {
					AsmGenerator generator = new AsmGenerator(controler
							.getTree(), controler.getSymbols());
					String asm = generator.generate();
					asemblerCodeText.setText(asm);
				} catch (Exception e1) {
					MessageBox mb = new MessageBox(shlCalc);
					mb.setMessage("Assembler generation failed");
					mb.setText("Error");
					mb.open();
				}
			}
		});
		evaluate.setText("Evaluate");

		Group grpAsembler = new Group(shlCalc, SWT.NONE);
		grpAsembler.setText("Assembler editor");
		grpAsembler.setLayout(new GridLayout(1, false));
		GridData gd_grpAsembler = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 3);
		gd_grpAsembler.widthHint = 193;
		gd_grpAsembler.heightHint = 357;
		grpAsembler.setLayoutData(gd_grpAsembler);

		asemblerCodeText = new AsmEditor(grpAsembler, SWT.BORDER | SWT.V_SCROLL);
		asemblerCodeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 3));

		Menu menu = new Menu(asemblerCodeText);
		asemblerCodeText.setMenu(menu);

		MenuItem mntmSaveAsFile = new MenuItem(menu, SWT.NONE);
		mntmSaveAsFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shlCalc, SWT.SAVE);
				fd.setFilterExtensions(new String[] { "*.as", "*.txt", "*.*" });
				String fileName = fd.open();
				if (fileName == null) {
					return;
				}

				try {
					FileWriter writeResult = new FileWriter(fileName);
					PrintWriter resultOutput = new PrintWriter(writeResult,
							true);
					resultOutput.println(asemblerCodeText.getText());
					resultOutput.close();
				} catch (IOException e1) {
					// TODO make fuction for message boxes;.
					MessageBox mb = new MessageBox(shlCalc);
					mb.setMessage("Error");
					mb.setText("Failed to save file");
					mb.open();
				}
			}
		});
		mntmSaveAsFile.setText("Save as file");

		Group grpTree = new Group(shlCalc, SWT.NONE);
		grpTree.setText("Result");
		grpTree.setLayout(new GridLayout(2, false));
		GridData gd_grpTree = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				1);
		gd_grpTree.widthHint = 294;
		gd_grpTree.heightHint = 33;
		grpTree.setLayoutData(gd_grpTree);

		result = new Text(grpTree, SWT.BORDER | SWT.READ_ONLY);
		result.setBackground(SWTResourceManager.getColor(255, 255, 255));
		GridData gd_result = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 1);
		gd_result.widthHint = 192;
		result.setLayoutData(gd_result);

		clear = new Button(grpTree, SWT.NONE);
		GridData gd_clear = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		gd_clear.widthHint = 78;
		clear.setLayoutData(gd_clear);
		clear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result.setText("");
				visualTree.getDrawerTree().removeAll();
				asemblerCodeText
						.setText(";; Press evaluate to generate assembler");
			}
		});
		clear.setText("Clear");

		Group grpResults = new Group(shlCalc, SWT.NONE);
		grpResults.setText("Tree");
		GridData gd_grpResults = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1);
		gd_grpResults.heightHint = 208;
		gd_grpResults.widthHint = 296;
		grpResults.setLayoutData(gd_grpResults);

		visualTree = new TreeDrawer(grpResults, SWT.NONE);
		visualTree.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_LIST_SELECTION_TEXT));
		visualTree.setBounds(10, 20, 282, 195);

		Menu menu_1 = new Menu(shlCalc, SWT.BAR);
		shlCalc.setMenuBar(menu_1);

		MenuItem mntmFile = new MenuItem(menu_1, SWT.NONE);
		mntmFile.setText("File");
		new Label(shlCalc, SWT.NONE);

		Button close = new Button(shlCalc, SWT.NONE);
		close.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlCalc.close();
			}
		});
		GridData gd_close = new GridData(SWT.RIGHT, SWT.TOP, false, false, 1, 1);
		gd_close.widthHint = 77;
		close.setLayoutData(gd_close);
		close.setText("Close");
	}
}
