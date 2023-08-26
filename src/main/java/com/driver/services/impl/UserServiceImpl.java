package com.driver.services.impl;

import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        Country country;
        try{
            country = findCountryByName(countryName);
        } catch(Exception e) {
            throw new Exception("Country not found");
        }

        User user = new User(username, password, false);
        user.setMaskedIp(null);

        User userWithId = userRepository3.save(user);
        userWithId.setOriginalIp(country.getCountryCode()+"."+userWithId.getId());

        return userRepository3.save(userWithId);
    }
    private Country findCountryByName(String countryName) throws Exception {
        Country country;
        if(countryName.equalsIgnoreCase("IND")) {
            country = new Country(CountryName.IND, "001");
        } else if(countryName.equalsIgnoreCase("USA")) {
            country = new Country(CountryName.USA, "002");
        } else if(countryName.equalsIgnoreCase("AUS")) {
            country = new Country(CountryName.AUS, "003");
        } else if(countryName.equalsIgnoreCase("CHI")) {
            country = new Country(CountryName.CHI, "004");
        } else if(countryName.equalsIgnoreCase("JPN")) {
            country = new Country(CountryName.JPN, "005");
        } else {
            throw new Exception("Country not found");
        }
        return country;
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        Optional<User> optionalUser = userRepository3.findById(userId);
        User user = optionalUser.get();

        Optional<ServiceProvider> optionalServiceProvider = serviceProviderRepository3.findById(serviceProviderId);
        ServiceProvider serviceProvider = optionalServiceProvider.get();

        user.getServiceProviderList().add(serviceProvider);
        serviceProvider.getUserList().add(user);

        serviceProviderRepository3.save(serviceProvider);
        return user;
    }
}
