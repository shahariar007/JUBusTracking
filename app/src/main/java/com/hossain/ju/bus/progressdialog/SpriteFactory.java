package com.hossain.ju.bus.progressdialog;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            default:
                break;
        }
        return sprite;
    }
}
