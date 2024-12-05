/**
 * App.java
 * 
 * Point d'entrée principal pour l'application EcoManager.
 * Contient la méthode `main` qui initialise et démarre le jeu.
 *
 * Auteur(s) : Yann RENARD, Yanis MEKKI
 */

import extensions.File;
import extensions.CSVFile;

class App extends Program {

    final String NOM = "EcoManager";
    final String TITRE = "../ressources/titre_ascii.txt";

    void algorithm(){
        start();
 //       if(choix==1 /*choix2 */){
 //           while(ville.tour<70 || ville.bonheur>50 || ville.pollution<100 || ville.budget>0){
 //               afficherEtatJeu(ville);


 //          }
        }
        
        

        
        
        // Boucle principale du jeu
        // while (budget >= /* valeur */ || pollution >= /* valeur */ || bonheur >= /* valeur */) {
        //     afficherEtatJeu(tour, budget, pollution, bonheur);
        //     tour++;
        // }
    
    void afficherMenuStart(){
        println("Choisissez une option :");
        println("1. Nouvelle ville");
        println("2. Charger une partie");
        println("3. Afficher les règles du jeu");
        println("4. Quitter");

    }
    void StartSelect(int choix){
        if(choix == 1){
            println("Quel est le nom de votre ville ?");
            String nomville = readString();
            City ville = creerpartie(nomville);
        }
        else if(choix == 2){
            println("Systeme de chargement de sauvegarde pas encore disponible");
        }
        else if(choix == 3){
            println("Règles");
            choixdeq();
            start();
        } 
        else if(choix == 4){
            println("A bientôt !");            
        }
    }

    int choixvalidenbr(int nbrchoix){
        print("Entrez votre choix (1-"+nbrchoix+") : ");
        int choix=readInt();
        while(choix<1 || choix>nbrchoix){
            println("La saisie est invalide veuillez recommencez !");
            choix=readInt();
        }
        return choix;
    }

    char choixdeq(){
        println("pour quitter la page de règles du jeu, appuyez sur 'q')");
        char choix_q=readChar();
        while(choix_q!='q'){
            println("La saisie est invalide veuillez recommencez !");
            choix_q=readChar();
        }
        return choix_q;
    }
    

    // Affichage de l'état du jeu
    void afficherEtatJeu(City ville) {
        println(ville.nom);
        println("---------------------------------");
        println("Tour : " + ville.tour);
        println("Budget : " + ville.budget + " €");
        println("Pollution : " + ville.pollution + "%");
        println("Bonheur : " + ville.bonheur + "%");
    }

    void afficherTitreJeu(String dessin){
        File file = newFile(dessin);
        while(ready(file)){
            println(readLine(file));
        }
    }  
    
    City creerpartie(String nom){
        City ville= new City();
        ville.nom=nom;
        ville.tour=1;
        ville.budget=45000;
        ville.pollution=20;
        ville.bonheur=85;
        return ville;
    }
    int tirerAuHasard(int max){
		return (int) (random()*max) ;
    }
    void start(){
        afficherTitreJeu(TITRE); // Affiche le titre du jeu
        afficherMenuStart(); // Affiche le menu principal
        int choix = choixvalidenbr(4);
        StartSelect(choix);
    }

    //Decisions decision(){
        

        
    //}
}

