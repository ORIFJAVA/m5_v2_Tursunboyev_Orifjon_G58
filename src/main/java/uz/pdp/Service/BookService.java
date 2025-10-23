package uz.pdp.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.Entity.Author;
import uz.pdp.Entity.Book;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static uz.pdp.Utils.AppUtils.intScanner;
import static uz.pdp.Utils.AppUtils.strScanner;

public class BookService {
    public static void run() throws IOException, URISyntaxException, InterruptedException {
        w:
        while (true) {
            showBookMenu();
            switch (intScanner.nextInt()) {
                case 1 -> showAllBooks();
                case 2 -> showSingleBookById();
                case 3 -> createNewBook();
                case 4 -> updateBook();
                case 5 -> deleteBook();
                case 6 -> authorNameIsBook();
                case 7 -> {
                    break w;
                }
                default -> System.out.println("Invalid Input");
            }
        }
    }

    private static void authorNameIsBook() {
    }

    private static void deleteBook() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter Book ID: ");
        Long bookId = intScanner.nextLong();

        String url = "http://localhost:5050/api/books/" + bookId;

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 204) {
            System.out.println("Book is deleted!");
        } else {
            System.out.println("Book not deleted!");
        }
        System.out.println("-------------------------");

    }

    private static void updateBook() throws IOException, InterruptedException, URISyntaxException {
        System.out.print("Enter Book ID to update: ");
        Long bookId = intScanner.nextLong();
        System.out.print("Enter new Title: ");
        String title = strScanner.nextLine();
        System.out.print("Enter new Publis Year: ");
        Integer publishYear = intScanner.nextInt();
        System.out.print("Enter new Price: ");
        Double price = intScanner.nextDouble();
        System.out.print("Enter Author ID: ");
        Long groupId = intScanner.nextLong();
        Author author = Author.builder().id(groupId).build();

        Book updatedBook = Book.builder()
                .title(title)
                .publishYear(publishYear)
                .price(price)
                .author(author)
                .build();

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(updatedBook);

        String url = "http://localhost:5050/api/books/" + bookId;

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        Book book = gson.fromJson(body, Book.class);
        System.out.println(book);
        System.out.println("Book is updated!: ");
        System.out.println("----------------");

    }

    private static void createNewBook() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter Book Title: ");
        String title = strScanner.nextLine();
        System.out.print("Enter Book Publish Year: ");
        Integer publishYear = intScanner.nextInt();
        System.out.print("Enter Book Price: ");
        Double price = intScanner.nextDouble();
        System.out.print("Enter Author Id: ");
        Long authorId = intScanner.nextLong();
        Author author = Author.builder().id(authorId).build();

        Book newBook = Book.builder()
                .title(title)
                .publishYear(publishYear)
                .price(price)
                .author(author)
                .build();

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(newBook);

        String url = "http://localhost:5050/api/books";

        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();

        if (statusCode == 201) {
            Book created = gson.fromJson(response.body(), Book.class);
            System.out.println(created);
            System.out.println("Book is created!");
        } else {
            System.out.println("Book is not created!");
        }
        System.out.println("----------------");

    }

    private static void showSingleBookById() throws IOException, InterruptedException, URISyntaxException {
        System.out.print("Enter Book ID: ");
        long bookId = intScanner.nextLong();

        String url = "http://localhost:5050/api/books/" + bookId;


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
            System.out.println(bookId + "= li book mavjud emas!");
        } else {
            Gson gson = new GsonBuilder().create();
            Book book = gson.fromJson(body, Book.class);
            System.out.println(book);
        }
        System.out.println("-------------------------");
    }

    private static void showAllBooks() throws IOException, InterruptedException, URISyntaxException {
        String url = "http://localhost:5050/api/books";


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

        Book[] books = gson.fromJson(body, Book[].class);
        for (Book book : books) {
            System.out.println(book);
        }
        System.out.println("----------------");


    }

    private static void showBookMenu() {
        System.out.println("""
                1. Show All Books
                2. Show single Book by id
                3. Create new Book
                4. Update Book
                5. Delete Book
                6. Author Name is Book
                7. Back to menu 
                """);
    }
}

