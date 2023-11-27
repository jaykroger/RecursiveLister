import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RecursiveListerGUI extends JFrame
{
    JPanel mainPanel;

    // Header panel assets
    JPanel headerPanel;
    JLabel header;
    Font headerFont = new Font("Comfortaa", Font.BOLD, 49);
    Color headerLabelColor = new Color(247, 246, 245, 255);
    Color headerBackgroundColor = new Color(28,33,40,255);


    // Main interface assets, which contains field to select file, as well as textArea to show extracted tags
    JPanel centerPanel;
    JPanel interfacePanel;
    JLabel fileLabel;
    JTextField fileTextField;
    JButton selectFileButton;
    JButton scanButton;
    JFileChooser fileChooser;


    // Display Panel
    JPanel displayPanel;
    JLabel filterStringLabel;
    JTextField filterStringField;
    JTextArea fileTextArea;
    JScrollPane filterScrollPane;
    Font interfaceLabelFont = new Font("Arial", Font.PLAIN, 12);
    Color interfaceLabelColor = new Color(167,180,192,255);
    Color interfaceBackgroundColor = new Color(34,39,46,255);
    Color interfaceButtonColor = new Color(186, 185, 180);
    Color interfaceButtonTextColor = new Color(35,37,45);


    // Button Panel
    JPanel buttonPanel;
    JButton clearButton;
    JButton quitButton;


    // Program logic variables
    File selectedDirectory;
    Path directory;


    public RecursiveListerGUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel();
        headerPanel.setBackground(headerBackgroundColor);
        headerPanel.setLayout(new GridBagLayout());
        Dimension headerSize = new Dimension(1080, 100);
        headerPanel.setPreferredSize(headerSize);

        header = new JLabel("Recursive File Lister");
        header.setFont(headerFont);
        header.setForeground(headerLabelColor);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        headerPanel.add(header, gbc);
    }

    private void createCenterPanel()
    {
        centerPanel = new JPanel();
        centerPanel.setBackground(interfaceBackgroundColor);
        centerPanel.setLayout(new BorderLayout());

        createInterfacePanel();
        centerPanel.add(interfacePanel, BorderLayout.PAGE_START);

        createDisplayPanel();
        centerPanel.add(displayPanel, BorderLayout.PAGE_END);
    }

    private void createInterfacePanel() {
        interfacePanel = new JPanel();
        interfacePanel.setBackground(interfaceBackgroundColor);
        interfacePanel.setLayout(new GridBagLayout());

        fileLabel = new JLabel("Directory:");
        fileLabel.setFont(interfaceLabelFont);
        fileLabel.setForeground(interfaceLabelColor);

        fileTextField = new JTextField();
        fileTextField.setColumns(50);

        selectFileButton = new JButton("Select Directory");
        selectFileButton.setFont(interfaceLabelFont);
        selectFileButton.setForeground(interfaceButtonTextColor);
        selectFileButton.setBackground(interfaceButtonColor);
        selectFileButton.addActionListener((ActionEvent ae) -> getSelectedFile());

        scanButton = new JButton("Scan");
        scanButton.setFont(interfaceLabelFont);
        scanButton.setForeground(interfaceButtonTextColor);
        scanButton.setBackground(interfaceButtonColor);
        scanButton.addActionListener((ActionEvent ae) -> scanDirectory(directory));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        interfacePanel.add(fileLabel, gbc);
        gbc.gridx = 1;
        interfacePanel.add(fileTextField, gbc);
        gbc.gridx = 2;
        interfacePanel.add(selectFileButton, gbc);
        gbc.gridx = 3;
        interfacePanel.add(scanButton, gbc);
    }

    private void createDisplayPanel()
    {
        displayPanel = new JPanel();
        displayPanel.setBackground(interfaceBackgroundColor);
        displayPanel.setLayout(new GridBagLayout());

        fileTextArea = new JTextArea();
        fileTextArea.setRows(10);
        fileTextArea.setColumns(70);
        fileTextArea.setLineWrap(false);
        fileTextArea.setWrapStyleWord(true);
        filterScrollPane = new JScrollPane(fileTextArea);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(75, 5, 5, 5);

        gbc.gridy = 1;
        gbc.gridx = 1;
        interfacePanel.add(filterScrollPane, gbc);
    }

    private void createButtonPanel() {
        buttonPanel = new JPanel();
        buttonPanel.setBackground(interfaceBackgroundColor);
        buttonPanel.setLayout(new GridBagLayout());

        clearButton = new JButton("Clear");
        clearButton.setFont(interfaceLabelFont);
        clearButton.setForeground(interfaceButtonTextColor);
        clearButton.setBackground(interfaceButtonColor);
        clearButton.addActionListener((ActionEvent ae) ->
        {
            fileTextField.selectAll();
            fileTextField.setText("");

            fileTextArea.selectAll();
            fileTextArea.setText("");
        });

        quitButton = new JButton("Quit");
        quitButton.setFont(interfaceLabelFont);
        quitButton.setForeground(interfaceButtonTextColor);
        quitButton.setBackground(interfaceButtonColor);
        quitButton.addActionListener((ActionEvent ae) -> getQuitConfirm());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 25, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(clearButton, gbc);
        gbc.gridx = 1;
        buttonPanel.add(quitButton, gbc);
    }

    private void getSelectedFile() {
        File workingDirectory = new File(System.getProperty("user.dir"));

        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(workingDirectory);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            selectedDirectory = fileChooser.getSelectedFile();
            directory = selectedDirectory.toPath();
            fileTextField.setText(String.valueOf(directory));
        }
    }

    private void scanDirectory(Path directoryPath)
    {
        if (fileTextField.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Please select a directory.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            fileTextArea.selectAll();
            fileTextArea.setText("");

            try {
                Files.list(directoryPath).forEach(path ->
                {
                    if (Files.isDirectory(path))
                    {
                        scanDirectory(path);
                    }
                    else
                    {
                        fileTextArea.append(path.toString() + "\n");
                    }
                });
            }
            catch (IOException e)
            {
                JOptionPane.showMessageDialog(null, "Error scanning directory.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void getQuitConfirm()
    {
        int quitConfirm;
        quitConfirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Exit", JOptionPane.YES_NO_OPTION);

        if (quitConfirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
