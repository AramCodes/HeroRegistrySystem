package com.theheroregistry.com.hero;

import com.theheroregistry.com.hero.View.HeroRegistrySystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/* Aram Valcourt
   CEN 3024C
   10/07/2025
   SOFTWARE DEVELOPMENT I
   Main Class running the HRS; The HRS purpose is to enable management and registry of the Hero HQ's heroes through a
   CLI driven app to allow C.R.U.D functionality and two custom methods
*/
@SpringBootApplication
public class HeroHq {


    /* art thanks to patorjk.com/software/taag/ */


	/*  Main method
        contains the logic to trigger welcome screen and run the HRS
        the parameter args takes in array of strings
        the return type is void.
    */
	public static void main(String[] args) {
		System.out.println("""
                 /$$   /$$                                     /$$$$$$$                      /$$             /$$                                /$$$$$$                        /$$                           \s
                | $$  | $$                                    | $$__  $$                    |__/            | $$                               /$$__  $$                      | $$                           \s
                | $$  | $$  /$$$$$$   /$$$$$$   /$$$$$$       | $$  \\ $$  /$$$$$$   /$$$$$$  /$$  /$$$$$$$ /$$$$$$    /$$$$$$  /$$   /$$      | $$  \\__/ /$$   /$$  /$$$$$$$ /$$$$$$    /$$$$$$  /$$$$$$/$$$$\s
                | $$$$$$$$ /$$__  $$ /$$__  $$ /$$__  $$      | $$$$$$$/ /$$__  $$ /$$__  $$| $$ /$$_____/|_  $$_/   /$$__  $$| $$  | $$      |  $$$$$$ | $$  | $$ /$$_____/|_  $$_/   /$$__  $$| $$_  $$_  $$
                | $$__  $$| $$$$$$$$| $$  \\__/| $$  \\ $$      | $$__  $$| $$$$$$$$| $$  \\ $$| $$|  $$$$$$   | $$    | $$  \\__/| $$  | $$       \\____  $$| $$  | $$|  $$$$$$   | $$    | $$$$$$$$| $$ \\ $$ \\ $$
                | $$  | $$| $$_____/| $$      | $$  | $$      | $$  \\ $$| $$_____/| $$  | $$| $$ \\____  $$  | $$ /$$| $$      | $$  | $$       /$$  \\ $$| $$  | $$ \\____  $$  | $$ /$$| $$_____/| $$ | $$ | $$
                | $$  | $$|  $$$$$$$| $$      |  $$$$$$/      | $$  | $$|  $$$$$$$|  $$$$$$$| $$ /$$$$$$$/  |  $$$$/| $$      |  $$$$$$$      |  $$$$$$/|  $$$$$$$ /$$$$$$$/  |  $$$$/|  $$$$$$$| $$ | $$ | $$
                |__/  |__/ \\_______/|__/       \\______/       |__/  |__/ \\_______/ \\____  $$|__/|_______/    \\___/  |__/       \\____  $$       \\______/  \\____  $$|_______/    \\___/   \\_______/|__/ |__/ |__/
                                                                                   /$$  \\ $$                                   /$$  | $$                 /$$  | $$                                           \s
                                                                                  |  $$$$$$/                                  |  $$$$$$/                |  $$$$$$/                                           \s
                                                                                   \\______/                                    \\______/                  \\______/                                            \s"""
		);

		new HeroRegistrySystem().run();
	}

}
