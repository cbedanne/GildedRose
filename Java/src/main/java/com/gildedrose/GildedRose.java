package com.gildedrose;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class GildedRose {
    Item[] items;

    private static final List<String> FastDegradingItems= Collections.singletonList("Conjured Mana Cake");
    private static final List<String> IncreasingItemsWhenApproachingSellIn= Arrays.asList("Aged Brie", "Backstage passes to a TAFKAL80ETC concert");
    private static final List<String> ConstantQuality= Collections.singletonList("Sulfuras, Hand of Ragnaros");

    private static final int QUALITY_UPPER_LIMIT = 50;
    private static final int QUALITY_LOWER_LIMIT = 0;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public List<Item> updateQuality() {

        List<Item> resultList = Arrays.stream(items)
                .filter(isItemInList(IncreasingItemsWhenApproachingSellIn))
                .peek(item -> item.quality = getIncreasingQuality(item))
                .peek(item -> item.quality = Math.min(item.quality, QUALITY_UPPER_LIMIT))
                .peek(item->item.sellIn=item.sellIn-1)
                .collect(Collectors.toList());

        List<Item> constantQualityItemList=Arrays.stream(items)
                .filter(item->ConstantQuality.contains(item.name))
                .collect(Collectors.toList());

        resultList.addAll(constantQualityItemList);

        List<Item> fastdegradingItemsList = Arrays.stream(items)
                .filter(isItemInList(FastDegradingItems))
                .peek(item->item.quality=item.quality-2)
                .peek(item->item.sellIn=item.sellIn-1)
                .collect(Collectors.toList());

        resultList.addAll(fastdegradingItemsList);

        List<Item> remainingItemList = Arrays.stream(items)
                .filter(isItemInList(resultList.stream().map(item->item.name).collect(Collectors.toList())).negate())
                .peek(item->item.quality=item.sellIn<0?item.quality-2:item.quality-1)
                .peek(item->item.sellIn=item.sellIn-1)
                .collect(Collectors.toList());

        resultList.addAll(remainingItemList);

        resultList= resultList.stream()
                .peek(item->item.quality= Math.min(item.quality, QUALITY_UPPER_LIMIT))
                .peek(item->item.quality= Math.max(item.quality, QUALITY_LOWER_LIMIT))
                .collect(Collectors.toList());

        return resultList;

    }

    private Predicate<Item> isItemInList(List<String> itemList){
        return item->itemList.contains(item.name);
    }


    private int getIncreasingQuality(Item item) {

        int updatedQuality=item.quality;

        if (item.sellIn<0){
            updatedQuality =0;
        } else if (item.sellIn<4) {
            updatedQuality=updatedQuality+3;
        }
        else if (item.sellIn<11) {
            updatedQuality= updatedQuality + 2;
        }else   {
            updatedQuality=updatedQuality+1;
        }

        return updatedQuality;
    }
}
