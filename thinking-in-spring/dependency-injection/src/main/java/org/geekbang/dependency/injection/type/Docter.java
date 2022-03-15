package org.geekbang.dependency.injection.type;

import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Properties;

/**
 * @author mao  2021/4/22 16:06
 */
public class Docter {
    private Long id;
    private String name;
    private CityEnum city;
    private Resource resource;

    private CityEnum[] workCitys;
    private List<CityEnum> lifeCitys;



    @Override
    public String toString() {
        return "Docter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city=" + city +
                ", resource=" + resource +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CityEnum getCity() {
        return city;
    }

    public void setCity(CityEnum city) {
        this.city = city;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public CityEnum[] getWorkCitys() {
        return workCitys;
    }

    public void setWorkCitys(CityEnum[] workCitys) {
        this.workCitys = workCitys;
    }

    public List<CityEnum> getLifeCitys() {
        return lifeCitys;
    }

    public void setLifeCitys(List<CityEnum> lifeCitys) {
        this.lifeCitys = lifeCitys;
    }
}
