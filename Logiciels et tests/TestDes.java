import java.util.Arrays;

public class TestDes
{

public static void main (String[] args)
{
	Des cryptage = new Des ();
	assert cryptage.bitsToString (cryptage.stringToBits ("Bonjour")).equals ("Bonjour");
	assert cryptage.bitsToString (cryptage.stringToBits ("Au revoir")).equals ("Au revoir");
	assert cryptage.bitsToString (cryptage.stringToBits ("Cryptage")).equals ("Cryptage");
	assert cryptage.bitsToString (cryptage.stringToBits (" (è'57/")).equals (" (è'57/");

	for (int i = 1; i <= 10; i ++)
	{
		int[] permutation = cryptage.generePermutation (10);
		int[] G = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		int[] copie_de_G = G.clone ();
		cryptage.permutation (permutation, G);
		cryptage.invPermutation (permutation, G);
		assert equals (copie_de_G, G) : "Test permutation" + Integer.toString (i);


	}

	int[] G = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	int[] copie_de_G = G.clone ();
	G = cryptage.recollage_bloc (cryptage.decoupage (G, 2));
	assert equals (copie_de_G, G) : "Test decoupage/recollage";

	G = new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	cryptage.decalle_gauche (G, 2);
	assert equals (G, new int[] {3, 4, 5, 6, 7, 8, 9, 10, 1, 2}) : "Test decalage";

	G = new int[] {1, 0, 1, 0};
	assert equals (cryptage.xor (G, G), new int[] {0, 0, 0, 0}) : "Test XOR 1";
	int[] H = {0, 0, 1, 1};
	assert equals (cryptage.xor (G, H), new int[] {1, 0, 0, 1}) : "Test XOR 2";

	String[] liste_mots = {"Bonjour", "Au revoir", "Cryptage", "&é'(-è_çà)=", "Ceci est une longue phrase"};
	for (String chaine: liste_mots) {
		String crypte_decrypte = cryptage.decrypte (cryptage.crypte (chaine));
		crypte_decrypte.replace ("\0", "");    // on retire les caracteres nuls a la fin d'un mot
		assert chaine_egales (chaine, crypte_decrypte) : crypte_decrypte.length ();
	}
	String B = "B";
	System.out.println (cryptage.decrypte (cryptage.crypte (B)));


	System.out.println ("Tous les tests sont passés !");
}

public static boolean equals (int[] G, int[] H)
{       // renvoie si 2 tableaux sont egaux
	if (G.length != H.length)
	{
		return false;
	}
	for (int i = 0; i < G.length; i ++)
	{
		if (G[i] != H[i]) return false;
	}
	return true;
}

public static boolean chaine_egales (String s1, String s2)
{
	String min = (s1.length () > s2.length ())? s2: s1;
	String max = (s1.length () > s2.length ())? s1: s2;
	for (int i = 0; i < min.length (); i ++) {
		if (s1.charAt (i) != s2.charAt (i)) {
			return false;
		}
	}
	return s1.length () == s2.length () || max.charAt (min.length ()) == '\0';
}

}
