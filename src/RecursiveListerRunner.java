import javax.swing.*;

public class RecursiveListerRunner
{
    public static void main(String[] args)
    {
        RecursiveListerGUI lister = new RecursiveListerGUI();

        lister.setTitle("Recursive File Lister");
        lister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        lister.setSize(1080, 660);
        lister.setLocation(85, 10);
        lister.setVisible(true);
    }
}