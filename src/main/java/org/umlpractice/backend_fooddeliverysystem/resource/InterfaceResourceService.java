package org.umlpractice.backend_fooddeliverysystem.resource;

public interface InterfaceResourceService {
    public ImageDTO saveImage(Image image) throws IllegalArgumentException;
    public ImageDTO loadImage(String imagePath) throws IllegalArgumentException;
    public void deleteImage(String imagePath) throws IllegalArgumentException;
}
