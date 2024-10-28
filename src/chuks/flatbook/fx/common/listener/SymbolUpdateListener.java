/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package chuks.flatbook.fx.common.listener;

import chuks.flatbook.fx.common.account.order.SymbolInfo;
import java.util.List;

/**
 *
 * @author user
 */
public interface SymbolUpdateListener {
    
    public void onSwapChange(SymbolInfo symbolInfo);
    public void onPriceChange(SymbolInfo symbolInfo);
    public void onSymbolInfoAdded(SymbolInfo symbolInfo);
    public void onSymbolInfoRemoved(SymbolInfo symbolInfo);
    public void onGetFullRefereshSymbol(String symbolName);

    public void onfullSymbolList(List<String> symbol_list);
    public void onSeletedSymbolList(List<String> symbol_list);
    public void onSeletedSymbolInfoList(List<SymbolInfo> symbol_info_list);
}
