package uz.pdp.Service;

import uz.pdp.Entity.Book;

import java.io.IOException;
import java.net.URISyntaxException;

import static uz.pdp.Utils.AppUtils.intScanner;

public class MainService {
    public static void run() throws URISyntaxException, IOException, InterruptedException {
        w:
        while (true) {
            showMainMenu();
            switch (intScanner.nextInt()) {
                case 1 -> AuthorService.run();
                case 2 -> BookService.run();
                case 3 -> {
                    System.out.println("Loyihamizni ishlatganigizga raxmat!");
                    break w;
                }
                default -> System.out.println("Unknown Input! Please try again!");

            }
        }
    }
    private static void showMainMenu() {
        System.out.println("""
                1.Author Service
                2.Book Service
                3.Exit
                """);
    }

}