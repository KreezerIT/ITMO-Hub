package util.appconfig;

import dao.persistence.OwnerDaoImpl;
import dao.persistence.PetDaoImpl;
import service.OwnerServiceImpl;
import service.PetServiceImpl;
import controller.PetController;
import controller.OwnerController;

public class App {

    public static void main(String[] args) {
        OwnerDaoImpl ownerDao = new OwnerDaoImpl();
        PetDaoImpl petDao = new PetDaoImpl();

        OwnerServiceImpl ownerService = new OwnerServiceImpl(ownerDao);
        PetServiceImpl petService = new PetServiceImpl(petDao);

        OwnerController ownerController = new OwnerController(ownerService);
        PetController petController = new PetController(petService, ownerService);

        AppUI ui = new AppUI(petController, ownerController);
        ui.start();
    }
}