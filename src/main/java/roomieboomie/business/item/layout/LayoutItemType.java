package roomieboomie.business.item.layout;

/**
 * Typ eines LayoutItems mit abrufbarer Textur
 */
public enum LayoutItemType {
    WALL, DOOR, WINDOW;

    private String texture;

    /**
     *
     * @return Textur des LayoutItemTypes
     */
    public String getTexture() {
        //TODO
        return texture;
    }

}
