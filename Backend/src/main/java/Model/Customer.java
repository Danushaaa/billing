/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class Customer {
    private String accountNumber;
    private String name;
    private String email;
    private String contact;
    private String address;
    private String NIC;
    private String password;
    private int unitsConsumed;

    public Customer() {}

    public Customer(String accountNumber, String name, String email, String contact, String address, String NIC, String password, int unitsConsumed) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
        this.NIC = NIC;
        this.password = password;
        this.unitsConsumed = unitsConsumed;
    }

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getNIC() { return NIC; }
    public void setNIC(String NIC) { this.NIC = NIC; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public int getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(int unitsConsumed) { this.unitsConsumed = unitsConsumed; }
}