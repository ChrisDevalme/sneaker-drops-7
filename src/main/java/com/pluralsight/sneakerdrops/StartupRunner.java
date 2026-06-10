package com.pluralsight.sneakerdrops;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.DropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class StartupRunner implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final SneakerRepository sneakerRepository;
    private final DropService dropService;

    @Autowired
    public StartupRunner(BrandRepository brandRepository, SneakerRepository sneakerRepository, DropService dropService) {
        this.brandRepository = brandRepository;
        this.sneakerRepository = sneakerRepository;
        this.dropService = dropService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
        System.out.println(dropService.getStatus());
        for (Brand brand : brandRepository.findAll()) {
            System.out.println(brand.getId() + " - " + brand.getName());
        }
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- Sneaker Library ---");
            System.out.println("1) List all sneakers");
            System.out.println("2) Find By Release Year");
            System.out.println("3) Find By model");
            System.out.println("4) Find By Price");
            System.out.println("5) Search");
            System.out.println("6) Find by Id");
            System.out.println("0) Quit");
            System.out.print("Your Choice: ");

            switch (scanner.nextInt()) {
                case 1 ->listSneakers();
                case 2 ->findByYear(scanner);
                case 3 ->findByModel(scanner);
                case 4 ->findByPrice(scanner);
                case 5 -> findBySearch(scanner);
                case 6 -> viewById(scanner);
                case 0 -> running = false;
                default -> System.out.println("Wrong Input!");
            }
        }
    }

    private void listSneakers() {
        System.out.println("---" +  sneakerRepository.count() + " Sneakers---");
        for (Sneaker s : sneakerRepository.findAll()) {
            System.out.println(s.getId() + " - " + s.getModel() + "(" + s.getPrice() + ")");
        }
    }

    private void findByYear(Scanner scanner) {
        System.out.print("Year: ");
        int year = scanner.nextInt();
        for (Sneaker s : sneakerRepository.findByReleaseYear(year)) {
            System.out.println(s.getModel() + " (" + s.getReleaseYear() + ")");
        }
    }
    private void findByModel(Scanner scanner) {
        scanner.nextLine(); // clear the leftover newline
        System.out.print("Model contains: ");
        String text = scanner.nextLine();
        for (Sneaker s : sneakerRepository.findByModelContaining(text)) {
            System.out.println(s.getModel());
        }
    }
    private void findByPrice(Scanner scanner) {
        System.out.print("Minimum Price: ");
        double min = scanner.nextDouble();
        for (Sneaker s : sneakerRepository.findByPriceLessThan(min)) {
            System.out.println(s.getModel() + " (" + s.getPrice() + ")");
        }
    }

    private void findBySearch(Scanner scanner){
        System.out.print("Enter your max price range: ");
        double price = scanner.nextDouble();

        System.out.print("Enter your min year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        for (Sneaker sneaker : sneakerRepository.search(price, year)) {
            System.out.printf("%s ($%.2f %d)%n", sneaker.getModel(), sneaker.getPrice(), sneaker.getReleaseYear());
        }

    }

    private void viewById(Scanner scanner){
        System.out.print("Enter Sneaker id: ");
        long id = scanner.nextLong();

        Sneaker sneaker = sneakerRepository.findById(id).orElse(null);

        if (sneaker == null) {
            System.out.println("No sneaker found with that id.");
        } else {
            System.out.printf("%d - %s ($%.2f)", sneaker.getId(), sneaker.getModel(), sneaker.getPrice());
        }
    }

    private void seedData() {
        if (brandRepository.count() == 0) {
            brandRepository.save(new Brand("Nike"));
            brandRepository.save(new Brand("Adidas"));
            brandRepository.save(new Brand("New Balance"));
            brandRepository.save(new Brand("Reebok"));
            brandRepository.save(new Brand("Prada"));
        }
        if (sneakerRepository.count() == 0) {
            sneakerRepository.save(new Sneaker("Air Force 1", 100, 1972));
            sneakerRepository.save(new Sneaker("Air Jordan 1", 180, 1973));
            sneakerRepository.save(new Sneaker("Prada Cup", 900, 2000));
            sneakerRepository.save(new Sneaker("Yeezy", 220, 2012));
            sneakerRepository.save(new Sneaker("Air Max", 180, 1985));
        }
    }
}