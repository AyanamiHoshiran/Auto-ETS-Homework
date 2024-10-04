package cn.ayanamihoshiran.autoetshomework.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GUI {

    private int guiWidth;
    private int guiHeight;
    private int guiX;
    private int guiY;

    public GUI(int guiX, int guiY, int guiWidth, int guiHeight) {
        this.guiX = guiX;
        this.guiY = guiY;
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
    }

}
