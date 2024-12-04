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

    void algorithm(){
        afficherTitreJeu(DESSIN); // Yann
        int choix=readInt();
        while(choix!=1 || choix!=2 || choix!=3 || choix!=4){
            println("La saisie est invalide veuillez recommencez !");
            choix=readInt();
        }
        if(choix==1){
            println("Quel est le nom de votre ville ?");
            String nomville=readString();
            City ville=creerpartie(nomville);
        }
        else if(choix==2){
            println("Systeme de chargement de sauvegarde pas encore disponible");
            //Charger partie quand dispo ville=""
        }
        else if(choix==3){
            println("Règles");
        }
        if(choix==1 || choix==2){
            while((ville.tour<70) || (ville.bonheur>50) || (ville.pollution<100) || (ville.budget>0)){
                afficherEtatJeu(ville);
                

            }
        }
        
        

        
        
        // Boucle principale du jeu
        // while (budget >= /* valeur */ || pollution >= /* valeur */ || bonheur >= /* valeur */) {
        //     afficherEtatJeu(tour, budget, pollution, bonheur);
        //     tour++;
        // }
    }

    

    // Affichage de l'état du jeu
    void afficherEtatJeu(City ville) {
        println(ville.nom)
        println("---------------------------------")
        println("Tour : " + ville.tour);
        println("Budget : " + ville.budget + " €");
        println("Pollution : " + ville.pollution + "%");
        println("Bonheur : " + ville.bonheur + "%");
    }

    void afficherTitreJeu(String dessin){
        File file = newFile(dessin);
        while(ready(files)){
            println(readLine(files));
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
}

