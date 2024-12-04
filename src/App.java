/**
 * App.java
 * 
 * Point d'entrée principal pour l'application EcoManager.
 * Contient la méthode `main` qui initialise et démarre le jeu.
 *
 * Auteur(s) : Yann RENARD, Yanis MEKKI
 */

import extensions.File;

import extensions.CSV;

class App extends Program {

    final String NOM = "EcoManager";
    final String DESSIN = "ressources/art_ascii.txt";
    final int TEMPS_PAUSE = 500;

    void algorithm() {
        // Initialisation des variables de jeu (A EQUILIBRER)
        int tour = 1;
        int budget = 45000;
        int pollution = 20;
        int bonheur = 85;


        // // Boucle principale du jeu
        // while (budget >= /* valeur */ || pollution >= /* valeur */ || bonheur >= /* valeur */) {
        //     afficherEtatJeu(tour, budget, pollution, bonheur);
        //     tour++;
        // }
    }

    // Affichage de l'état du jeu
    void afficherEtatJeu(int tour, int budget, int pollution, int bonheur) {
    }
}