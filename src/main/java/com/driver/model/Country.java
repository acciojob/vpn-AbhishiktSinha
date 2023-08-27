// Note: Do not write @Enumerated annotation above CountryName in this model.
package com.driver.model;

import javax.persistence.*;

@Entity
public class Country{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private CountryName countryName;

    private String code;

    @OneToOne(mappedBy = "country", cascade = CascadeType.ALL)
    User user;

    @ManyToOne
    @JoinColumn
    ServiceProvider serviceProvider;

    public Country(CountryName countryName, String code) {
        this.countryName = countryName;
        this.code = code;
    }
    public Country() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CountryName getCountryName() {
        return countryName;
    }

    public void setCountryName(CountryName countryName) {
        this.countryName = countryName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ServiceProvider getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void enrich(String countryName) throws Exception {
        if(countryName.equalsIgnoreCase("IND")) {
            this.setCountryName(CountryName.IND);
            this.setCode(CountryName.IND.toCode());
        } else if(countryName.equalsIgnoreCase("USA")) {
            this.setCountryName(CountryName.USA);
            this.setCode(CountryName.USA.toCode());
        } else if(countryName.equalsIgnoreCase("AUS")) {
            this.setCountryName(CountryName.AUS);
            this.setCode(CountryName.AUS.toCode());
        } else if(countryName.equalsIgnoreCase("CHI")) {
            this.setCountryName(CountryName.CHI);
            this.setCode(CountryName.CHI.toCode());
        } else if(countryName.equalsIgnoreCase("JPN")) {
            this.setCountryName(CountryName.JPN);
            this.setCode(CountryName.JPN.toCode());
        } else {
            throw new Exception("Country not found");
        }
    }
}
