package org.umlpractice.backend_fooddeliverysystem.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * ResourceServiceImplement 类说明
 *
 * @author 刘陈文君
 * @date 2025/6/29 15:14
 */
@Service
public class ResourceServiceImplement implements InterfaceResourceService{
    @Autowired
    private InterfaceResourceDAO interfaceResourceDAO;

    @Value("${app.image.url}")
    private static String path;

    @Override
    public ImageDTO saveImage(Image image) throws IllegalArgumentException
    {
        if(this.interfaceResourceDAO.save(image)!=null)
        {
            return new ImageDTO(image);
        }
        else
        {
            throw new IllegalArgumentException("Can't save image at "+image.getStrImageDirectory());
        }
    }

    @Override
    public ImageDTO loadImage(String imagePath) throws IllegalArgumentException
    {
        int i=0;
        for(;i<=imagePath.length()-1;i++)
        {
            if(imagePath.charAt(i)=='.')
                break;
        }
        String strImageId = imagePath.substring(0,i);
        Integer imageId = Integer.parseInt(strImageId);
        Optional<Image> ret = this.interfaceResourceDAO.findById(imageId);
        if(ret.isPresent())
        {
            return new ImageDTO(ret.get());
        }
        else
        {
            throw new IllegalArgumentException("Can't find image for image id "+strImageId);
        }
    }

    @Override
    public void deleteImage(String imagePath) throws IllegalArgumentException
    {
        int i=0;
        for(;i<=imagePath.length()-1;i++)
        {
            if(imagePath.charAt(i)=='.')
                break;
        }
        String strImageId = imagePath.substring(i-1);
        Integer imageId = Integer.parseInt(strImageId);
        this.interfaceResourceDAO.deleteById(imageId);
    }
}
