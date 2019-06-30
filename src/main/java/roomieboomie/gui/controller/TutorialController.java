package roomieboomie.gui.controller;


public class TutorialController {
    private RootController switcher;


    public void backToMnu(){
        switcher.switchView("MainMenu");
    }
    public void setSwitcher(RootController rootController){
        this.switcher = rootController;
    }

}
