package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception{
        Optional<User> optionalUser = userRepository2.findById(userId);
        User user = optionalUser.get();

        if(user.getConnected() == true) {
            throw new Exception("Already connected");
        }
        else if(user.getOriginalCountry().getCountryName().toString() == countryName) {
            return user;
        }
        else {
            List<ServiceProvider> serviceProviderList = user.getServiceProviderList();
            if(serviceProviderList.size() == 0) {
                throw new Exception("Unable to connect");
            }

            for(ServiceProvider serviceProvider : serviceProviderList) {
                List<Country> sp_countryList = serviceProvider.getCountryList();

                for(Country sp_country : sp_countryList) {
                    if(sp_country.getCountryName().toString().equalsIgnoreCase(countryName)) {
                        //establish connection
                        Connection connection = new Connection();
                        connection.setUser(user);
                        connection.setServiceProvider(serviceProvider);
                        Connection connectionWithId = connectionRepository2.save(connection);

                        user.setConnected(true);
                        user.setVpnCountry(sp_country);
                        user.setMaskedIp(sp_country.getCode() + "." + serviceProvider.getId() + "." + user.getId());

                        serviceProvider.getConnectionList().add(connectionWithId);
                        user.getConnectionList().add(connectionWithId);

                        userRepository2.save(user);
                        serviceProviderRepository2.save(serviceProvider);

                        return user;
                    }
                }
            }

            //no service provider offers vpn to the country
            throw new Exception("Unable to connect");
        }


    }
    @Override
    public User disconnect(int userId) throws Exception {
        Optional<User> optionalUser = userRepository2.findById(userId);
        User user = optionalUser.get();

        if(user.getConnected() == false) {
            throw new Exception("Already disconnected");
        }

        user.setVpnCountry(null);
        user.setMaskedIp(null);
        user.setConnected(null);

        return user;
    }
    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        Optional<User> optionalSender = userRepository2.findById(senderId);
        Optional<User> optionalReceiver = userRepository2.findById(receiverId);

        User sender = optionalSender.get();
        User receiver = optionalReceiver.get();

        String receiverCountry;
        if(receiver.getConnected() == true) {
            receiverCountry = receiver.getVpnCountry().toString();
        } else
            receiverCountry = receiver.getOriginalCountry().toString();


        if(sender.getOriginalCountry().toString().equalsIgnoreCase(receiverCountry)) {
            return sender;
        }
        if(sender.getOriginalCountry().toString().equalsIgnoreCase(receiverCountry)) {
            return sender;
        }

        //find suitable vpn for sender
        try {
            connectSenderToVPN(sender, receiverCountry);
            return sender;
        } catch(Exception e) {
            throw new Exception("Cannot establish communication");
        }
    }

    private void connectSenderToVPN(User sender, String receiverCountry) throws Exception {
        List<ServiceProvider> serviceProviderList = sender.getServiceProviderList();
        if(serviceProviderList.size() == 0) {
            throw new Exception("Unable to connect");
        }

        int sp_smallestID = Integer.MAX_VALUE;

        for(ServiceProvider serviceProvider : serviceProviderList) {
            List<Country> sp_countryList = serviceProvider.getCountryList();
            int sp_Id = serviceProvider.getId();

            if(sp_Id >= sp_smallestID)
                continue;

            for(Country sp_country : sp_countryList) {

                String availableCountry = sp_country.getCountryName().toString();

                if(availableCountry.equalsIgnoreCase(receiverCountry)) {

                    //establish connection
                    Connection connection = new Connection();
                    connection.setUser(sender);
                    connection.setServiceProvider(serviceProvider);
                    Connection connectionWithId = connectionRepository2.save(connection);

                    sender.setConnected(true);
                    sender.setVpnCountry(sp_country);
                    sender.setMaskedIp(sp_country.getCode() + "." + serviceProvider.getId() + "." + sender.getId());

                    serviceProvider.getConnectionList().add(connectionWithId);
                    sender.getConnectionList().add(connectionWithId);

                    userRepository2.save(sender);
                    serviceProviderRepository2.save(serviceProvider);

                }
            }
        }

        //no service provider offers vpn to the country
        throw new Exception("Unable to connect");
    }
}
