package util.appconfig;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.PetController;
import controller.OwnerController;
import entity.Pet;
import entity.Owner;
import util.PetColor;

public class AppUI {

    private final PetController petController;
    private final OwnerController ownerController;
    private final Scanner scanner;

    public AppUI(PetController petController, OwnerController ownerController) {
        this.petController = petController;
        this.ownerController = ownerController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1 -> createOwner();
                case 2 -> createPet();
                case 3 -> createPetFriendship();
                case 4 -> showPetsOfOwner();
                case 5 -> showFriendsOfPet();
                case 6 -> showAllPetsWithFriends();
                case 7 -> showAllOwnersWithPets();
                case 8 -> updateOwner();
                case 9 -> updatePet();
                case 10 -> deleteOwnerById();
                case 11 -> deletePetById();
                case 12 -> deleteOwnerByEntity();
                case 13 -> deletePetByEntity();
                case 14 -> deleteAllPets();
                case 15 -> deleteAllOwners();
                case 0 -> {
                    System.out.println("PokaBlyat!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n------ Menu ------");
        System.out.println("1.  Create Owner");
        System.out.println("2.  Create Pet for Owner");
        System.out.println("3.  Create Pet's Friendship");
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("4.  Show Pets of Owner");
        System.out.println("5.  Show Friends of Pet");
        System.out.println("6.  Show all pets (with friends)");
        System.out.println("7.  Show all owners (with pets)");
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("8.  Update Owner");
        System.out.println("9.  Update Pet");
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("10. Delete Owner by Id (with his pets)");
        System.out.println("11. Delete Pet by Id");
        System.out.println("12. Delete Owner by Entity");
        System.out.println("13. Delete Pet by Entity");
        System.out.println("14. Delete all Pets");
        System.out.println("15. Delete all Owners (with all pets)");
        System.out.println("- - - - - - - - - - - - - - - - - -");
        System.out.println("0. Exit");
        System.out.print("Your option: ");
    }

    private void deleteOwnerByEntity() {
        System.out.print("Enter owner's name: ");
        String name = scanner.nextLine();

        List<Owner> matchingOwners = ownerController.getOwnersByName(name);

        if (matchingOwners.isEmpty()) {
            System.out.println("No owners found with name: " + name);
            return;
        }

        System.out.println("Matching owners:");
        for (int i = 0; i < matchingOwners.size(); i++) {
            Owner o = matchingOwners.get(i);
            System.out.printf("%d. %s (ID: %d, Birthdate: %s, Pets: %d)\n",
                    i + 1, o.getName(), o.getId(), o.getBirthDate(), o.getPets().size());
        }

        System.out.print("Enter number of owner to delete: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > matchingOwners.size()) {
            System.out.println("Invalid selection");
            return;
        }

        Owner selectedOwner = matchingOwners.get(choice - 1);
        ownerController.deleteByEntity(selectedOwner);

        System.out.println("Owner deleted successfully");
    }

    private void deletePetByEntity() {
        System.out.print("Enter pet's name: ");
        String name = scanner.nextLine();

        List<Pet> matchingPets = petController.getPetsByName(name);

        if (matchingPets.isEmpty()) {
            System.out.println("No pets found with the name: " + name);
            return;
        }

        System.out.println("Matching pets:");
        for (int i = 0; i < matchingPets.size(); i++) {
            Pet pet = matchingPets.get(i);
            System.out.printf("%d. %s (ID: %d, Breed: %s, Color: %s, Owner: %s)\n",
                    i + 1, pet.getName(), pet.getId(), pet.getBreed(), pet.getColor(),
                    pet.getOwner() != null ? pet.getOwner().getName() : "No owner");
        }

        System.out.print("Enter the number of the pet to delete: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > matchingPets.size()) {
            System.out.println("Invalid selection");
            return;
        }

        Pet selectedPet = matchingPets.get(choice - 1);
        petController.deletePetByEntity(selectedPet);

        System.out.println("Pet deleted successfully");
    }

    private void showAllPetsWithFriends() {
        List<Pet> pets = petController.getAllPets();
        Map<Long, List<Pet>> allFriends = petController.getAllPetsFriends();

        for (Pet pet : pets) {
            System.out.println("- |Name|: " + pet.getName() + " |Breed|: " + pet.getBreed() + " |BirthDate|: " + pet.getBirthDate());
            List<Pet> friends = allFriends.getOrDefault(pet.getId(), List.of());
            if (!friends.isEmpty()) {
                System.out.println("  Friends:");
                for (Pet friend : friends) {
                    System.out.println("    ~ " + friend.getName() + " (ID: " + friend.getId() + ")");
                }
            }
        }
    }

    private void showFriendsOfPet() {
        System.out.print("Enter pet's ID: ");
        long petId = scanner.nextLong();
        scanner.nextLine();
        Pet pet = petController.getPetById(petId);

        if (pet != null) {
            List<Pet> friends = petController.getFriendsOf(pet);
            if (friends.isEmpty()) {
                System.out.println(pet.getName() + " has no friends");
            } else {
                System.out.println("Friends of " + pet.getName() + ":");
                for (Pet friend : friends) {
                    System.out.println("~ " + friend.getName() + "(ID: " + friend.getId() + ")");
                }
            }
        } else {
            System.out.println("Pet not found");
        }
    }

    private void createPetFriendship() {
        List<Pet> allPets = petController.getAllPets();
        if (allPets.size() < 2) {
            System.out.println("Not enough pets to create friendship");
            return;
        }

        System.out.println("Available pets:");
        allPets.forEach(p -> System.out.println(p.getId() + ": " + p.getName()));

        System.out.print("Enter first pet ID: ");
        Long id1 = Long.parseLong(scanner.nextLine());

        System.out.print("Enter second pet ID: ");
        Long id2 = Long.parseLong(scanner.nextLine());

        if (id1.equals(id2)) {
            System.out.println("A pet cannot be friends with itself");
            return;
        }

        Pet pet1 = petController.getPetById(id1);
        Pet pet2 = petController.getPetById(id2);

        if (pet1 == null || pet2 == null) {
            System.out.println("One or both pets not found");
            return;
        }

        petController.createPetFriendship(pet1, pet2);
        System.out.println(pet1.getName() + " and " + pet2.getName() + " are now friends");
    }


    private void showAllOwnersWithPets() {
        List<Owner> owners = ownerController.getAllOwners();

        if (owners.isEmpty()) {
            System.out.println("No owners found");
        } else {
            for (Owner owner : owners) {
                System.out.println("- |ID|: " + owner.getId() + " |Name|: " + owner.getName() + " |BirthDate|: " + owner.getBirthDate());
                var pets = owner.getPets();

                if (!pets.isEmpty()) {
                    System.out.println(" |Pets|:");
                    for (Pet pet : pets) {
                        System.out.println("  ~ " + pet.getName());
                    }
                }
            }
        }
    }

    private void createOwner() {
        System.out.print("Enter owner's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter owner's birth date (yyyy-MM-dd): ");
        String birthDate = scanner.nextLine();
        ownerController.createOwner(name, birthDate);
        System.out.println("Owner created successfully");
    }

    private void createPet() {
        System.out.print("Enter pet's name: ");
        String name = scanner.nextLine();
        System.out.print("Enter pet's birth date (yyyy-MM-dd): ");
        String birthDate = scanner.nextLine();
        System.out.print("Enter pet's breed: ");
        String breed = scanner.nextLine();
        System.out.print("Enter pet's color: ");
        String colorInput = scanner.nextLine();
        System.out.print("Enter owner's ID: ");
        long ownerId = scanner.nextLong();
        scanner.nextLine();

        PetColor color;
        try {
            color = PetColor.valueOf(colorInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid color entered: " + colorInput);
            return;
        }
        Pet newPet = petController.createPet(name, birthDate, breed, color, ownerId);
        if (newPet != null) {
            System.out.println("Pet created successfully");
        } else {
            System.out.println("Owner not found. Pet not created");
        }
    }

    private void showPetsOfOwner() {
        System.out.print("Enter owner's ID: ");
        long ownerId = scanner.nextLong();
        scanner.nextLine();
        Owner owner = ownerController.getOwnerById(ownerId);
        if (owner != null) {
            System.out.println("Pets of " + owner.getName() + ":");
            for (Pet pet : owner.getPets()) {
                System.out.println("- " + pet.getName());
            }
        } else {
            System.out.println("Owner not found");
        }
    }

    private void updateOwner() {
        System.out.print("Enter owner's ID to update: ");
        long ownerId = scanner.nextLong();
        scanner.nextLine();

        Owner existingOwner = ownerController.getOwnerById(ownerId);
        if (existingOwner == null) {
            System.out.println("Owner not found");
            return;
        }
        System.out.println("~Enter new data (leave blank to keep current)~ ");

        System.out.println("Current name: " + existingOwner.getName());
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        if (newName.isBlank()) newName = existingOwner.getName();

        System.out.println("Current birth date: " + existingOwner.getBirthDate());
        System.out.print("Enter new birth date (yyyy-MM-dd): ");
        String newBirthDateStr = scanner.nextLine();
        String finalBirthDate = newBirthDateStr.isBlank() ? existingOwner.getBirthDate().toString() : newBirthDateStr;

        Owner updatedOwner = ownerController.updateOwner(ownerId, newName, finalBirthDate);
        if (updatedOwner != null) {
            System.out.println("Owner updated successfully");
        } else {
            System.out.println("Update failed");
        }
    }

    private void updatePet() {
        System.out.print("Enter pet's ID to update: ");
        long petId = scanner.nextLong();
        scanner.nextLine();

        Pet existingPet = petController.getPetById(petId);
        if (existingPet == null) {
            System.out.println("Pet not found");
            return;
        }
        System.out.print("~Enter new data (leave blank to keep current)~ ");

        System.out.println("Current name: " + existingPet.getName());
        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        if (newName.isBlank()) newName = existingPet.getName();

        System.out.println("Current breed: " + existingPet.getBreed());
        System.out.print("Enter new breed: ");
        String newBreed = scanner.nextLine();
        if (newBreed.isBlank()) newBreed = existingPet.getBreed();

        System.out.println("Current color: " + existingPet.getColor());
        System.out.print("Enter new color: ");
        String newColorInput = scanner.nextLine();
        PetColor newColor = existingPet.getColor();

        if (!newColorInput.isBlank()) {
            try {
                newColor = PetColor.valueOf(newColorInput.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid color entered: " + newColorInput);
                return;
            }
        }

        System.out.println("Current owner ID: " + existingPet.getOwner().getId());
        System.out.print("Enter new owner's ID: ");
        String newOwnerIdStr = scanner.nextLine();
        long newOwnerId = newOwnerIdStr.isBlank()
                ? existingPet.getOwner().getId()
                : Long.parseLong(newOwnerIdStr);

        Pet updatedPet = petController.updatePet(petId, newName, "", newBreed, newColor, newOwnerId);
        if (updatedPet != null) {
            System.out.println("Pet updated successfully");
        } else {
            System.out.println("Update failed");
        }
    }


    private void deleteOwnerById() {
        System.out.print("Enter owner's ID to delete: ");
        long ownerId = scanner.nextLong();
        scanner.nextLine();
        ownerController.deleteOwnerById(ownerId);
        System.out.println("Owner and all his pets have been deleted");
    }


    private void deletePetById() {
        System.out.print("Enter pet's ID to delete: ");
        long petId = scanner.nextLong();
        scanner.nextLine();
        petController.deletePetById(petId);
        System.out.println("Pet deleted");
    }

    private void deleteAllPets() {
        petController.deleteAll();
        System.out.println("All pets deleted");
    }

    private void deleteAllOwners() {
        ownerController.deleteAll();
        System.out.println("All owners deleted");
    }
}
