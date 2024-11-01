import java.awt.event.*;
import java.io.*;
import java.lang.Math;
import java.util.*;
import javax.swing.*;

public class EcouteurBouton implements MouseListener
{

// Conversions de bases
public static final char[] chiffresHex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};


// Attributs
private String action;
private MenuPrincipal fenetrePrincipale;
private MenuExporter fenetreExporter;
private MenuImporter fenetreImporter;
private JTextField entree;
private JLabel sortie;

private Des crypteur;

// Constructeur
public EcouteurBouton (String action, MenuPrincipal fenetrePrincipale)
{
	this.action = action;
	this.fenetrePrincipale = fenetrePrincipale;
}

// Constructeurs avec entrée
public EcouteurBouton (String action, MenuExporter fenetreExporter, JTextField entree)
{
	this.action = action;
	this.fenetreExporter = fenetreExporter;
	this.entree = entree;
}
public EcouteurBouton (String action, MenuImporter fenetreImporter, JTextField entree)
{
	this.action = action;
	this.fenetreImporter = fenetreImporter;
	this.entree = entree;
}

// Constructeur avec E/S
public EcouteurBouton (String action, MenuPrincipal fenetrePrincipale, JTextField entree, JLabel sortie)
{
	this.action = action;
	this.fenetrePrincipale = fenetrePrincipale;
	this.entree = entree;
	this.sortie = sortie;
}


public static Boolean hex (String code)
{
	Boolean drapeau = false;

	for (char chiffre: code.toCharArray ())
	{
		drapeau = drapeau || (
			chiffre != '0' &&
			chiffre != '1'
			);
	}

	return drapeau;
}


public static String bitsToHex (int[] code)
{
	String resultat = "";
	int taille = code.length;
	int chiffreDec = 0;
	int bit, poidsBit;

	for (int pos = 0; pos < taille; pos ++)
	{
		// Comptage du chiffre hexadécimal
		bit = code[pos];
		poidsBit = 3 - pos % 4;
		chiffreDec += bit * Math.pow (2, poidsBit);

		// Ajout du chiffre hexadécimal
		if (poidsBit == 0)
		{
			resultat += chiffresHex[chiffreDec];
			chiffreDec = 0;
		}
	}

	return resultat;
}


public static int[] hexToBits (String code)
{
// Données
	HashMap <Character, String> bitsHex = new HashMap <>();
	bitsHex.put (' ', "");
	bitsHex.put ('0', "0000");
	bitsHex.put ('1', "0001");
	bitsHex.put ('2', "0010");
	bitsHex.put ('3', "0011");
	bitsHex.put ('4', "0100");
	bitsHex.put ('5', "0101");
	bitsHex.put ('6', "0110");
	bitsHex.put ('7', "0111");
	bitsHex.put ('8', "1000");
	bitsHex.put ('9', "1001");
	bitsHex.put ('A', "1010");
	bitsHex.put ('B', "1011");
	bitsHex.put ('C', "1100");
	bitsHex.put ('D', "1101");
	bitsHex.put ('E', "1110");
	bitsHex.put ('F', "1111");

// Conversion en binaire
	String binaire = "";

	if (hex (code))
	{
		String quatreBits = "";

		for (char chiffreHex: code.toCharArray ())
		{
			quatreBits = bitsHex.get (chiffreHex);
			binaire += quatreBits;
		}
	}
	else
	{
		binaire = code;
	}


// Passage de la chaîne de caractère à la liste contigüe
	int indice = 0;
	int[] bits = new int[binaire.length ()];

	for (char bit: binaire.toCharArray ())
	{
		bits[indice] = Character.digit (bit, 2);
		indice ++;
	}

	return bits;
}


// Opérations sur le dossier temporaire

// Écriture
public void stockage (String dossier, String nom)
{
	try
	{
		FileOutputStream fichierSortie = new FileOutputStream (dossier + '/' + nom + ".txt");
		ObjectOutputStream out = new ObjectOutputStream (fichierSortie);

		out.writeObject (this.crypteur);
		out.flush ();
		out.close ();
	}
	catch (FileNotFoundException unused)
	{
		System.out.println ("Écriture impossible : le fichier " + dossier + '/' + nom + ".txt n'a pas été trouvé.");
	}
	catch (IOException unused)
	{
		System.out.println ("Écriture impossible : IOException");
	}
}

// Lecture
public void telechargement (String dossier, String nom)
{
	try
	{
		FileInputStream fichierEntree = new FileInputStream (dossier + '/' + nom + ".txt");
		ObjectInputStream in = new ObjectInputStream (fichierEntree);

		this.crypteur = (Des) in.readObject ();
		in.close ();
	}
	catch (FileNotFoundException unused)
	{
		System.out.println ("Lecture impossible : le fichier " + dossier + '/' + nom + ".txt n'a pas été trouvé.");
	}
	catch (IOException unused)
	{
		System.out.println ("Lecture impossible : IOException");
	}
	catch (ClassNotFoundException unused)
	{
		System.out.println ("Lecture impossible : la classe Des.class n'a pas été trouvée.");
	}
}


// Événement du bouton cliqué
@Override
public void mouseClicked (MouseEvent e)
{
	switch (this.action)
	{

	case "encrypter":

		// Téléchargement de la clef ou génération d'une nouvelle
		if (new File ("temp/import_key.txt").exists ())
		{
			telechargement ("temp", "import_key");
		}
		else
		{
			this.crypteur = new Des ();
		}

		// Encryptage
		String message_a = this.entree.getText ();

		int[] bits_a = this.crypteur.crypte (message_a);

		String affichage = bitsToHex (bits_a);

		// Stockage de la clef en vue d'une future exportation
		stockage ("temp", "export_key");

		System.out.println (affichage);
		this.sortie.setText ("    " + affichage);

		break;


	case "decrypter":

		// Téléchargement de la clef ou génération d'une nouvelle
		if (new File ("temp/import_key.txt").exists ())
		{
			telechargement ("temp", "import_key");
		}
		else
		{
			this.crypteur = new Des ();
		}

		// Décryptage
		String code = this.entree.getText ();
		int[] bits_b = hexToBits (code);
		String message_b = this.crypteur.decrypte (bits_b);

		// Stockage de la clef en vue d'une future exportation
		stockage ("temp", "export_key");

		System.out.println (message_b);
		this.sortie.setText ("    " + message_b);

		break;


	case "menuExporter":

		MenuExporter fenetreExporter = new MenuExporter ();
		break;


	case "menuImporter":

		MenuImporter fenetreImporter = new MenuImporter ();
		break;


	case "exporter":

		String nomS = this.entree.getText ();

		telechargement ("temp", "export_key");
		stockage ("datas", nomS);

		this.fenetreExporter.dispose ();

		break;


	case "importer":

		String nomE = this.entree.getText ();

		telechargement ("datas", nomE);
		stockage ("temp", "import_key");

		this.fenetreImporter.dispose ();

		break;

	}
}

@Override
public void mousePressed (MouseEvent e)
{
}

@Override
public void mouseReleased (MouseEvent e)
{
}

@Override
public void mouseEntered (MouseEvent e)
{
}

@Override
public void mouseExited (MouseEvent e)
{
}

public static void main (String[] args)
{
}

}
