import java.awt.*;
import java.io.File;
import javax.swing.*;


public class MenuImporter extends JFrame
{

// Type des éléments
public JLabel titre;
private JTextField saisie;
private JButton valider;


// Constructeur
public MenuImporter ()
{
	// Titre
	super ("Importer");

	// Paramètres de la fenêtre
	this.setSize (500, 500);
	this.setResizable (false);

	this.setLayout (new BorderLayout ());

    // Liste des fichiers clefs à gauche
    String[] fichiers = datas ();
    int nbFichiers = fichiers.length;
    JPanel colonneFichiers = new JPanel (new GridLayout (nbFichiers, 1));

    JLabel caseNom;

    for (String nom: fichiers)
    {
        colonneFichiers.add (new JLabel (nom));
    }

    // Sélection du fichier à droite
	JPanel elements = new JPanel (new GridLayout (3, 1));

	this.titre = new JLabel (" Entrez ci-dessous le nom du fichier choisi (sans son format)... ");
	this.saisie = new JTextField ();

    this.valider = new JButton ("Télécharger");
	this.valider.addMouseListener (new EcouteurBouton ("importer", this, this.saisie));

	elements.add (titre);
	elements.add (saisie);
	elements.add (valider);

	// Ajout des 2 colonnes à l'interface
	this.add (colonneFichiers, BorderLayout.WEST);
    this.add (elements, BorderLayout.EAST);

	this.setVisible (true);
}


// Retourne le nombre de fichiers clefs dans datas
public static String[] datas ()
{
    File dossier = new File ("datas");
    return dossier.list ();
}


public static void main (String[] args)
{
	MenuImporter fenetre = new MenuImporter ();
}

}
