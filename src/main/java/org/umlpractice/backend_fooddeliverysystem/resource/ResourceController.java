package org.umlpractice.backend_fooddeliverysystem.resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * ResourceController 类说明
 *
 * @author 刘陈文君
 * @date 2025 /6/29 15:28
 */

class ImageInputObject
{
    private String strImageSuffix;
    private String strImageBase64Data;

    @Override
    public String toString() {
        return "ImageInputObject{" +
                "strImageSuffix='" + strImageSuffix + '\'' +
                ", strImageBase64Data='" + strImageBase64Data + '\'' +
                '}';
    }

    /**
     * Gets str image suffix.
     *
     * @return the str image suffix
     */
    public String getStrImageSuffix() {
        return strImageSuffix;
    }

    /**
     * Sets str image suffix.
     *
     * @param strImageSuffix the str image suffix
     */
    public void setStrImageSuffix(String strImageSuffix) {
        this.strImageSuffix = strImageSuffix;
    }

    /**
     * Gets str image base 64 data.
     *
     * @return the str image base 64 data
     */
    public String getStrImageBase64Data() {
        return strImageBase64Data;
    }

    /**
     * Sets str image base 64 data.
     *
     * @param strImageBase64Data the str image base 64 data
     */
    public void setStrImageBase64Data(String strImageBase64Data) {
        this.strImageBase64Data = strImageBase64Data;
    }
}

@Controller
@RequestMapping("/api/resource")
public class ResourceController {
    /**
     * The Interface resource service.
     */
    @Autowired
    InterfaceResourceService interfaceResourceService;

    /**
     * Post image image dto.
     *
     * @return the image dto
     */
    @PostMapping("images/")
    public Object postImage(@RequestBody ImageInputObject imageInputObject)
    {
        try {
            Image image = new Image();
            image.setStrImageSuffix(imageInputObject.getStrImageSuffix());
            image.setStrBase64Data(imageInputObject.getStrImageBase64Data());
            image.setStrImageDirectory("");
            ImageDTO ret = interfaceResourceService.saveImage(image);
            if(ret == null)
            {
                HashMap<String,Object> map = new HashMap<>();
                map.put("error","Unable to upload image");
                return ResponseEntity.badRequest().body(map);
            }
            return ResponseEntity.ok().body(ret);
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,Object> ret = new HashMap<>();
            ret.put("error",e.getMessage());
            return ResponseEntity.badRequest().body(ret);
        }
    }

    @GetMapping("/images/{url}")
    public Object getImage(@PathVariable("url") String url)
    {
        try
        {
            ImageDTO res = this.interfaceResourceService.loadImage(url);
            if(res == null)
            {
                HashMap<String,Object> map = new HashMap<>();
                map.put("error","Unable to load image");
                return ResponseEntity.badRequest().body(map);
            }
            return ResponseEntity.ok().body(res);
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,Object> map = new HashMap<>();
            map.put("error","Unable to get image for url "+url);
            return ResponseEntity.badRequest().body(map);
        }
    }

    public Object deleteImage(@PathVariable String url)
    {
        try
        {
            this.interfaceResourceService.deleteImage(url);
            HashMap<String,Object> map = new HashMap<>();
            map.put("msg","Deleted image successfully");
            return ResponseEntity.ok().body(map);
        }
        catch(IllegalArgumentException e)
        {
            HashMap<String,Object> map = new HashMap<>();
            map.put("error","Unable to delete image");
            return ResponseEntity.badRequest().body(map);
        }
    }
}
