package com.example.demo.dto;

import com.example.demo.entity.Item;
import lombok.Getter;

@Getter
public class ItemResponseDto {

    private final Long ItemId;

    private final String itemName;

    private final String description;

    private final String managerNickName;

    private final String ownerNickName;

    public ItemResponseDto(Long itemId, String itemName, String description, String managerNickName, String ownerNickName) {
        ItemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.managerNickName = managerNickName;
        this.ownerNickName = ownerNickName;
    }

    public static ItemResponseDto toDto(Item item) {
        return new ItemResponseDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getManager().getNickname(),
                item.getOwner().getNickname()
        );
    }
}
