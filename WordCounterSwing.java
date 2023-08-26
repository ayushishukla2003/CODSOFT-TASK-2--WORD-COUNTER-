
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.*;


public class WordCounterSwing extends JFrame {
    private JTextArea textArea;
    private JLabel wordCountLabel;
    private JLabel uniqueWordCountLabel;
    private JTextField inputTextField;
    private JTextArea statisticsTextArea;
    private JTextArea wordFrequencyTextArea; // New JTextArea for word frequencies
 
    private JButton closeFileButton; 

    public WordCounterSwing() {
        setTitle("Word Counter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        JPanel fileDataPanel = new JPanel(new BorderLayout());

        // Create a titled border with a larger font size for the heading
        TitledBorder titledBorder = BorderFactory.createTitledBorder(null, "File Data", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 16));
        fileDataPanel.setBorder(BorderFactory.createCompoundBorder(titledBorder, BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Set the titled border

        // Create a text area for displaying the file data
        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);




        wordCountLabel = new JLabel("Word Count: ");
        uniqueWordCountLabel = new JLabel("Unique Word Count: ");
        inputTextField = new JTextField();

        Container contentPane = getContentPane();

        


        JButton loadFileButton = new JButton("Load File");
        loadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile();
            }
        });



         // Add a "Close File" button
        JButton closeFileButton = new JButton("Close File");
         closeFileButton.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
                 closeFile();
             }
         });






                 JButton countWordsButton = new JButton("Count Words");
        countWordsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countWords(inputTextField.getText());
            }
        });

        statisticsTextArea = new JTextArea(10, 20);
        statisticsTextArea.setEditable(false);
        JScrollPane statisticsScrollPane = new JScrollPane(statisticsTextArea);

        JPanel statsPanel = new JPanel(new GridLayout(2, 1));
        statsPanel.add(wordCountLabel);
        statsPanel.add(uniqueWordCountLabel);
        statsPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(loadFileButton, BorderLayout.NORTH);
        topPanel.add(closeFileButton, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.setBackground(Color.WHITE);


       
        JPanel buttonPanel = new JPanel(new FlowLayout()); // Panel to hold buttons


        buttonPanel.add(loadFileButton);
        buttonPanel.add(closeFileButton); 
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.setBackground(Color.WHITE);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputTextField, BorderLayout.CENTER);
        bottomPanel.add(countWordsButton, BorderLayout.EAST);
        bottomPanel.setBackground(Color.WHITE);

        JPanel inputStatisticsPanel = new JPanel(new BorderLayout());
        inputStatisticsPanel.add(bottomPanel, BorderLayout.SOUTH);
        inputStatisticsPanel.add(statisticsScrollPane, BorderLayout.CENTER);
        inputStatisticsPanel.setBackground(Color.WHITE);

        // Create a JPanel to hold the word frequency display
        JPanel wordFrequencyPanel = new JPanel(new BorderLayout());
        TitledBorder wordFrequencyBorder = BorderFactory.createTitledBorder("Word Frequency");
        wordFrequencyPanel.setBorder(wordFrequencyBorder);

        wordFrequencyTextArea = new JTextArea(10, 40);
        wordFrequencyTextArea.setEditable(false);
        JScrollPane wordFrequencyScrollPane = new JScrollPane(wordFrequencyTextArea);

        wordFrequencyPanel.add(wordFrequencyScrollPane, BorderLayout.CENTER);

        // Add components to the main content pane
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPanel, BorderLayout.WEST);
     
        contentPane.add(fileDataPanel, BorderLayout.CENTER);
        contentPane.add(wordFrequencyPanel, BorderLayout.SOUTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(inputStatisticsPanel, BorderLayout.EAST);


    
        


        setVisible(true);
    }



    private void closeFile() {
        textArea.setText(""); // Clear the text area
        wordCountLabel.setText("Word Count: ");
        uniqueWordCountLabel.setText("Unique Word Count: ");
        statisticsTextArea.setText("");
    }







    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            try {
                Scanner scanner = new Scanner(selectedFile);
                StringBuilder fileContent = new StringBuilder();

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    fileContent.append(line).append("\n");
                }

                textArea.setText(fileContent.toString());
                countWords(fileContent.toString() + " " + inputTextField.getText()); // Combine both text areas

                scanner.close();
            } catch (FileNotFoundException ex) {
                showErrorDialog("File not found.");
            }
        }
    }

    private void countWords(String text) {
        String[] words = text.split("\\s+|\\p{Punct}+");
        int wordCount = 0;
        Set<String> uniqueWords = new HashSet<>();
        Map<String, Integer> wordFrequency = new HashMap<>();

        for (String word : words) {
            if (!word.isEmpty()) {
                wordCount++;
                uniqueWords.add(word.toLowerCase());
                wordFrequency.put(word.toLowerCase(), wordFrequency.getOrDefault(word.toLowerCase(), 0) + 1);
            }
        }

        wordCountLabel.setText("Word Count: " + wordCount);
        uniqueWordCountLabel.setText("Unique Word Count: " + uniqueWords.size());

        StringBuilder statisticsText = new StringBuilder();
        statisticsText.append("Word Frequency:\n");
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            statisticsText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        statisticsTextArea.setText(statisticsText.toString());

        StringBuilder wordFrequencyText = new StringBuilder();
        wordFrequencyText.append("Word Frequency:\n");
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            wordFrequencyText.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        wordFrequencyTextArea.setText(wordFrequencyText.toString());
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WordCounterSwing();
            }
        });
    }
}
