/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author Asier Vila Dominguez
 */
@Entity
@Table(name = "users", schema = "testreto2")
public class User implements Serializable {
    
    @Id
    private String username;
    private String fullName;
    private String password;
    private String phoneNumber;
    private String email;
    private Timestamp lastAccess;
    private Timestamp lastPasswordChange;
    private UserType type;
    @ManyToMany
    private Set<Color> likedColors;
    @ManyToMany
    private Set<Material> likedMaterials;
    @ManyToMany
    protected Set<Garment> garments;
    
    public User () {
        
    }
    
    public String getUsername () {
        return username;
    }

    public void setUsername (String username) {
        this.username = username;
    }
    
    public String getFullName () {
        return fullName;
    }

    public void setFullName (String fullName) {
        this.fullName = fullName;
    }

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    public String getPhoneNumber () {
        return phoneNumber;
    }

    public void setPhoneNumber (String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public Timestamp getLastAccess () {
        return lastAccess;
    }

    public void setLastAccess (Timestamp lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Timestamp getLastPasswordChange () {
        return lastPasswordChange;
    }

    public void setLastPasswordChange (Timestamp lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }

    public UserType getType () {
        return type;
    }

    public void setType (UserType type) {
        this.type = type;
    }

    public Set<Color> getLikedColors () {
        return likedColors;
    }

    public void setLikedColors (Set<Color> likedColors) {
        this.likedColors = likedColors;
    }

    public void addLikedColor (Color color) {
        
        if (!this.likedColors.contains(color)) {
            this.likedColors.add(color);
        }
    }
    
    public void removeLikedColor (Color color) {
        
        if (this.likedColors.contains(color)) {
            this.likedColors.remove(color);
        }
    }
    
    public Set<Material> getLikedMaterials () {
        return likedMaterials;
    }

    public void setLikedMaterials (Set<Material> likedMaterials) {
        this.likedMaterials = likedMaterials;
    }
    
    public void addLikedMaterial (Material material) {
        
        if (!this.likedMaterials.contains(material)) {
            this.likedMaterials.add(material);
        }
    }
    
    public void removeLikedMaterial (Material material) {
        
        if (this.likedMaterials.contains(material)) {
            this.likedMaterials.remove(material);
        }
    }

    public Set<Garment> getGarments () {
        return garments;
    }

    public void setGarments (Set<Garment> garments) {
        this.garments = garments;
    }
    
    @Override
    public int hashCode () {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.username);
        hash = 29 * hash + Objects.hashCode(this.type);
        hash = 29 * hash + Objects.hashCode(this.likedColors);
        hash = 29 * hash + Objects.hashCode(this.likedMaterials);
        hash = 29 * hash + Objects.hashCode(this.garments);
        return hash;
    }

    @Override
    public boolean equals (Object obj) {
        
        if (obj == null) {
            return false;
        }
        
        User casted = (User) obj;
        
        if (!casted.getUsername().equals(this.username)) {
            return false;
        }
        
        if (!casted.getFullName().equals(this.fullName)) {
            return false;
        }
        
        if (!casted.getEmail().equals(this.email)) {
            return false;
        }
        
        if (!casted.getPhoneNumber().equals(this.phoneNumber)) {
            return false;
        }
        
        return true;
    }

    @Override
    public String toString () {
        
        return this.username + ", " + this.fullName;
    }    
}