package uz.pdp.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.Entity.Author;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static uz.pdp.Utils.AppUtils.intScanner;
import static uz.pdp.Utils.AppUtils.strScanner;

public class AuthorService {
    public static void run() throws URISyntaxException, IOException, InterruptedException {
        w:
        while (true) {
            showAuthorMenu();
            switch (intScanner.nextInt()) {
                case 1 -> showAllAuthors();
                case 2 -> showSingleAuthorById();
                case 3 -> createNewAuthor();
                case 4 -> updateAuthor();
                case 5 -> deleteAuthor();
                case 6 -> authorByCountry();
                case 7 -> {
                    break w;
                }
                default -> System.out.println("Invalid Input");
            }
        }
    }

    private static void authorByCountry() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter author by Country: ");
        String country = strScanner.nextLine();

        String url = "http://localhost:5050/api/authors";

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();

        Gson gson = new GsonBuilder().create();
        Author[] authors = gson.fromJson(body, Author[].class);

        List<Author> authorsList = List.of(authors);

        List<Author> filteredAuthors = authorsList.stream()
                .filter(a -> a.getCountry().equals(country))
                .toList();
        if (filteredAuthors.isEmpty()) {
            System.out.println("Bu shahardan author topilmadi!");
        } else {
            System.out.println(filteredAuthors);
        }
        System.out.println("-------------------------");
    }


    private static void deleteAuthor() throws IOException, InterruptedException, URISyntaxException {
        System.out.print("Enter Author ID: ");
        long authorId = intScanner.nextLong();

        String url = "http://localhost:5050/api/authors/" + authorId;


        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());


        int statusCode = response.statusCode();
        if (statusCode == 204) {
            System.out.println("Author is deleted!");
        } else {
            System.out.println("Author is not deleted!");
        }
        System.out.println("-------------------------");

    }

    private static void updateAuthor() throws IOException, InterruptedException, URISyntaxException {
        System.out.print("Enter Author ID: ");
        Long authorId = intScanner.nextLong();

        System.out.print("Enter Author New Name: ");
        String name = strScanner.nextLine();
        System.out.print("Enter Author New Birth Year: ");
        Integer birthYear = intScanner.nextInt();
        System.out.print("Enter Author New Country:");
        String country = strScanner.nextLine();

        Author newAuthor = Author.builder().
                name(name).
                birthYear(birthYear).
                country(country).
                build();

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(newAuthor);

        String url = "http://localhost:5050/api/authors/" + authorId;

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2).build();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());


        String body = response.body();
        Author author = gson.fromJson(body, Author.class);
        System.out.println(author);
        System.out.println("Author is updated!");
        System.out.println("----------------");

    }

    private static void createNewAuthor() throws IOException, InterruptedException, URISyntaxException {
        System.out.print("Enter Author Name: ");
        String name = strScanner.nextLine();
        System.out.print("Enter Author Birth Year: ");
        Integer birthYear = intScanner.nextInt();
        System.out.print("Enter Author Country: ");
        String country = strScanner.nextLine();

        Author newAuthor = Author.builder().
                name(name).
                birthYear(birthYear).
                country(country).
                build();

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(newAuthor);


        String url = "http://localhost:5050/api/authors";


        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2).build();


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());


        String body = response.body();
        Author group = gson.fromJson(body, Author.class);
        System.out.println(group);
        System.out.println("New Author Created");
        System.out.println("----------------");


    }

    private static void showSingleAuthorById() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter Author ID: ");
        long authorId = intScanner.nextLong();

        String url = "http://localhost:5050/api/authors" + authorId;


        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());


        String body = response.body();

        if (body == null || body.isEmpty()) {
            System.out.println(authorId + "= Id li author mavjud emas!");
        } else {
            Gson gson = new GsonBuilder().create();
            Author author = gson.fromJson(body, Author.class);
            System.out.println(author);
        }
        System.out.println("-------------------------");


    }

    private static void showAllAuthors() throws URISyntaxException, IOException, InterruptedException {
        String url = "http://localhost:5050/api/authors";


        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2).build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());


        String body = response.body();

        Gson gson = new GsonBuilder().create();

        Author[] authors = gson.fromJson(body, Author[].class);
        for (Author group : authors) {
            System.out.println(group);
        }

        System.out.println("----------------");


    }

    private static void showAuthorMenu() {
        System.out.println("""
                1. Show All Authors
                2. Show single Author by id
                3. Create new Author
                4. Update Author
                5. Delete Author
                6. Country by Author
                7. Back to menu 
                """);
    }

}



