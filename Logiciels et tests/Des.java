import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// https://docs.google.com/document/d/1d1MSsNxa0R5r0Any7Z5dnsYuAVXWZDeR4vGL8cdD258/edit

public class Des implements Serializable {
final int taille_bloc = 64;
final int taille_sous_bloc = 32;
final int nb_ronde = 16;
final int[] tab_decalage = new int[nb_ronde];    // 16
final int[] perm_initiale = new int[64];    // a modifier
final int[][] S = new int[4][16];
final int[] E = {0, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16,
						17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 0, 1};
int[] masterKey;
int[][] tab_cles;

public Des() {
	this.masterKey = new int[64];
	this.tab_cles = new int[nb_ronde][48];
	Random rng = new Random ();
	for (int i = 0; i < this.masterKey.length; i ++) {
		if (rng.nextFloat () < 0.5) {
			this.masterKey[i] = 0;
		}
		else {
			this.masterKey[i] = 1;
		}
	}

	for (int i = 0; i < tab_decalage.length; i ++) {
		tab_decalage[i] = rng.nextInt (2) + 1;
	}
	int[] variable = generePermutation (taille_bloc);
	for (int i = 0; i < taille_bloc; i ++) {
		perm_initiale[i] = variable[i];
	}
	for (int i = 0; i < nb_ronde; i ++) {
		this.genereClef (i);
	}

	int[] base = new int[16];
	for (int i = 0; i < base.length; i ++) {
		base[i] = i;
	}
	for (int i = 0; i < 4; i ++) {
		int[] clone = base.clone ();
		this.permutation (generePermutation (16), clone);
		S[i] = clone;
	}

}

public int[] stringToBits (String message){
	while (message.length () % 8 != 0) {
		message += '\0';
	}
	int taille = message.length ();
	Integer code;
	int test;
	String baseDeux;
	int[] tableau = new int[taille * 8];
	int cpt = 0;

	for (char caractere: message.toCharArray ())
	{
		code = (int) caractere;
		baseDeux = Integer.toBinaryString (code);
		while (! (baseDeux.length () == 8)) {
			baseDeux = "0" + baseDeux;
		}

		for (char bit: baseDeux.toCharArray ())
		{
			tableau[cpt] = Integer.valueOf ("" + bit);
			cpt ++;
		}
	}

	return tableau;
}





public String bitsToString (int[] blocks) {
	int taille_bloc = 8;
	String s = "";
	for (int j = 0; j < blocks.length / taille_bloc; j ++) {
		int somme = 0;
		for (int i = 0; i < 8; i ++) {
			somme += Math.pow (2, 7 - i) * blocks[i + taille_bloc * j];
		}
		String to_add = String.valueOf ((char) somme);
		s = s.concat (to_add);
	}
	return s;
}




public int[] generePermutation (int taille) {
	ArrayList <Integer> tab_base = new ArrayList <Integer>();
	for (int i = 0; i < taille; i ++) {
		tab_base.add (i);   // tableau avec tous les nombres de 0 à taille-1 dans l'ordre
	}
	int[] tab_final = new int[taille];
	for (int i = 0; i < taille; i ++) {
		int index = (int) Math.floor (Math.random () * tab_base.size ());
		tab_final[i] = tab_base.get (index);
		tab_base.remove (index);
	}
	return tab_final;

}

public void permutation (int[] tab_permutation, int[] bloc) {
	int[] copie_bloc = bloc.clone ();
	for (int i = 0; i < bloc.length; i ++) {
		bloc[i] = copie_bloc[tab_permutation[i]];
	}
}

public void invPermutation (int[] tab_permutation, int[] bloc) {
	int[] copie_permute = bloc.clone ();
	for (int i = 0; i < bloc.length; i ++) {
		bloc[tab_permutation[i]] = copie_permute[i];
	}
}

public int[][] decoupage (int[] bloc, int nbBlocs){
	if (bloc.length % nbBlocs != 0) {
		System.out.println ("erreur, la division en partie égale est impossible");
	}
	int my_taille_bloc = bloc.length / nbBlocs;
	int[][] resultat = new int[nbBlocs][my_taille_bloc];
	for (int i = 0; i < nbBlocs; i ++) {
		for (int j = 0; j < my_taille_bloc; j ++) {
			resultat[i][j] = bloc[i * my_taille_bloc + j];
		}
	}
	return resultat;
}

public int[] recollage_bloc (int[][] blocs) {
	int[] resultat = new int[blocs.length * blocs[0].length];
	for (int i = 0; i < blocs.length; i ++) {
		for (int j = 0; j < blocs[0].length; j ++) {
			resultat[i * blocs[0].length + j] = blocs[i][j];

		}
	}
	return resultat;
}

public void decalle_gauche (int[] bloc, int nbCrans) {
	int[] copie_bloc = bloc.clone ();
	for (int i = 0; i < bloc.length; i ++) {
		int index = i - nbCrans;
		if (index < 0) index += bloc.length;
		bloc[index] = copie_bloc[i];
	}
}

public void genereClef (int nbRonde){

	int[][] deuxBlocs;
	int[] clef = Arrays.copyOfRange (masterKey, 0, 56);

	permutation (generePermutation (56), clef);

	deuxBlocs = decoupage (clef, 2);

	// Décalage de chaque bloc
	decalle_gauche (deuxBlocs[0], tab_decalage[nbRonde]);
	decalle_gauche (deuxBlocs[1], tab_decalage[nbRonde]);

	clef = recollage_bloc (deuxBlocs);
	permutation (generePermutation (56), clef);
	clef = Arrays.copyOfRange (clef, 0, 48);
	tab_cles[nbRonde] = clef;
}

public int[] xor (int[] tab1, int[] tab2)
{
	int ele1, ele2;
	int taille = tab1.length;

	int[] resultat = new int[taille];

	for (int index = 0; index < taille; index ++)
	{
		ele1 = tab1[index];
		ele2 = tab2[index];

		// corps du xor
		resultat[index] = (ele1 + ele2) % 2;
	}

	return resultat;
}


public int[] fonction_S (int[] tab) {
	String ligne = Integer.toString (tab[0]) + Integer.toString (tab[5]);
	String colonne = "";
	for (int i = 1; i < 5; i ++) {
		colonne += Integer.toString (tab[i]);
	}
	int element = S[Integer.parseInt (ligne, 2)][Integer.parseInt (colonne, 2)];
	String s = Integer.toBinaryString (element);
	char[] tab_char = s.toCharArray ();
	int[] result = new int[4];
	for (int i = 0; i < tab_char.length; i ++) {
		result[i + 4 - (tab_char.length)] = Integer.parseInt (Character.toString (tab_char[i]));
	}
	return result;


}

public int[] fonction_F (int[] uneClef, int[] unD)
{
	int[] Dn_extd = new int[48];
	for (int i = 0; i < 48; i ++) {
		Dn_extd[i] = unD[E[i]];
	}
	int[] dEtoile = xor (Dn_extd, uneClef);
	int[][] huitBlocs = decoupage (dEtoile, 8);

	// Passage dans la fonction S
	int cpt = 0;
	int[] bloc32 = new int[32];

	for (int[] bloc: huitBlocs) {
		// Recollage
		for (int bit: fonction_S (bloc))
		{
			bloc32[cpt] = bit;
			cpt ++;
		}
	}

	// Sortie du tableau de 32 bits
	return bloc32;
}

public int[] crypte (String message_clair){
	/*
	 * Initialisation
	 */

	int[][] parties;
	int[] G, D, Kn, blocFinal;

	// Transfo du message en blocs de bits
	int[] bits = stringToBits (message_clair);
	int taille = bits.length;
	int[][] blocs = decoupage (bits, taille / 64);

	for (int t = 0; t < 3; t ++) {// triple DES
		/*
		 * Traitement*/

		for (int[] bloc: blocs) {
			// PrÃ©paration des 2 parties
			permutation (perm_initiale, bloc);
			parties = decoupage (bloc, 2);
			G = parties[0];
			D = parties[1];
			for (int ronde = 0; ronde < nb_ronde; ronde ++) {
				// Changement des parties nb_ronde fois
				Kn = this.tab_cles[ronde];
				int[] copie = D.clone ();
				D = xor (G, fonction_F (Kn, D));
				G = copie;
			}

			// Permutation inverse des 2 parties
			int[][] a_recoller = {G, D};
			int[] copie_bloc = recollage_bloc (a_recoller);
			for (int i = 0; i < bloc.length; i ++) {
				bloc[i] = copie_bloc[i];     // pour des raisons de copies
			}
			invPermutation (perm_initiale, bloc);
		}
	}
	// Jointure des blocs en un tableau
	int[] table = recollage_bloc (blocs);



	/*
	 * Sortie
	 */
	// System.out.println(Arrays.toString(table));
	// L'on retourne ce tableau
	return table;
}


public String decrypte (int[] bits){
	/*
	 * Initialisation
	 */
	int[][] parties;
	int[] G, D, Kn, copie;
	int[] blocFinal;
	// Séparation en blocs
	int taille = bits.length;

	int[][] blocs = decoupage (bits, taille / 64);
	/*
	 * Traitement
	 */
	for (int t = 0; t < 3; t ++) { // triple DES
		for (int[] bloc: blocs) {
			// Préparation des 2 parties
			permutation (perm_initiale, bloc);
			parties = decoupage (bloc, 2);
			G = parties[0];
			D = parties[1];
			// so far so good
			for (int ronde = 0; ronde < nb_ronde; ronde ++)
			{

				Kn = tab_cles[nb_ronde - 1 - ronde];
				copie = D.clone ();    // copie = Dn+1
				D = G.clone ();   // D = Gn+1
				G = xor (copie, fonction_F (Kn, D));
				// Changement des parties nb_ronde fois
				/*Kn = genereClef (ronde);
				   int[] copie = D;
				   D = xor (G, fonction_F(Kn, D));
				   G = copie;*/

			}
			// Permutation inverse des 2 parties
			int[][] a_recoller = {G, D};
			copie = recollage_bloc (a_recoller);
			for (int i = 0; i < copie.length; i ++) {
				bloc[i] = copie[i];
			}
			invPermutation (perm_initiale, bloc);

		}
	}

	// Jointure des blocs en un tableau
	int[] table = recollage_bloc (blocs);

	/*
	 * Sortie
	 */

	// Transfo du blocs de bits en message
	String message_clair = bitsToString (table);

	// L'on retourne ce tableau
	return message_clair;
}

}
