package org.example.onlineshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import org.example.onlineshop.model.enumerations.UserType;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String lastName;

    private String username;

    private String password;

    private String streetNumber;

    private String houseNumber;

    private float discount;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemInCart> cartItems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemRating> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders;

    public User(String name, String lastName, String username, String password, String streetNumber, String houseNumber) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.streetNumber = streetNumber;
        this.houseNumber = houseNumber;
        this.discount = 0.0F;
        this.type = UserType.USER;
        this.cartItems = new HashSet<>();
        this.ratings = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public float getDiscount() {
        return discount;
    }

    public UserType getType() {
        return type;
    }

    public Set<ItemInCart> getCartItems() {
        return cartItems;
    }

    public Set<ItemRating> getRatings() {
        return ratings;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    //    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singleton(() -> "ROLE_" + type.name());
//    }
//
//    @Override
//    public String getPassword() {
//        return null;
//    }
//
//    @Override
//    public String getUsername() {
//        return null;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
}
