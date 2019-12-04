/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package litfitsserver.entities;

import java.io.Serializable;

/**
 * Enum that defines where this garment can be worn
 * @author Charlie
 */
public enum BodyPart implements Serializable {
    TOP, BOTTOM, SHOE, HAT, OTHER, FULLBODY;
}
