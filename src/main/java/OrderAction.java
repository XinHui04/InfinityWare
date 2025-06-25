/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public abstract class OrderAction {
    protected String[] orderParts;
    protected Path filePath;
    protected int lineIndex;
    protected List<String> username;
    public OrderAction(String[] orderParts, Path filePath, int lineIndex, List<String> username) {
        this.orderParts = orderParts;
        this.filePath = filePath;
        this.lineIndex = lineIndex;
        this.username = username;
    }
    public abstract void execute() throws IOException;
}