package com.st.newtest.poeGame.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ken
 * @since 2020-11-02
 */
@Entity
public class PoeItemPrice implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

    private String itemName;

    private String itemType;

    private String itemCurPrice;

    private String itemDesc;

    private String itemRecordTime;

    
    public Long getId() {
        return id;
    }

      public void setId(Long id) {
          this.id = id;
      }
    
    public String getItemName() {
        return itemName;
    }

      public void setItemName(String itemName) {
          this.itemName = itemName;
      }
    
    public String getItemType() {
        return itemType;
    }

      public void setItemType(String itemType) {
          this.itemType = itemType;
      }
    
    public String getItemCurPrice() {
        return itemCurPrice;
    }

      public void setItemCurPrice(String itemCurPrice) {
          this.itemCurPrice = itemCurPrice;
      }
    
    public String getItemDesc() {
        return itemDesc;
    }

      public void setItemDesc(String itemDesc) {
          this.itemDesc = itemDesc;
      }
    
    public String getItemRecordTime() {
        return itemRecordTime;
    }

      public void setItemRecordTime(String itemRecordTime) {
          this.itemRecordTime = itemRecordTime;
      }

    @Override
    public String toString() {
        return "PoeItemPrice{" +
              "id=" + id +
                  ", itemName=" + itemName +
                  ", itemType=" + itemType +
                  ", itemCurPrice=" + itemCurPrice +
                  ", itemDesc=" + itemDesc +
                  ", itemRecordTime=" + itemRecordTime +
              "}";
    }
}
