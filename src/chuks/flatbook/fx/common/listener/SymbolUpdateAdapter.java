/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chuks.flatbook.fx.common.listener;

import chuks.flatbook.fx.common.account.order.SymbolInfo;
import java.awt.Component;
import java.util.List;

/**
 *
 * @author user
 */
public abstract class SymbolUpdateAdapter implements SymbolUpdateListener{

    private Component comp;

    public SymbolUpdateAdapter() {
    }

    public SymbolUpdateAdapter(Component comp) {
        this.comp = comp;
    }
    
    public Component getComponent(){
        return comp;
    }
    
    @Override
    public void onSwapChange(SymbolInfo symbolInfo) {        
    }

    @Override
    public void onPriceChange(SymbolInfo symbolInfo) {        
    }

    @Override
    public void onSymbolInfoAdded(SymbolInfo symbolInfo) {        
    }

    @Override
    public void onSymbolInfoRemoved(SymbolInfo symbolInfo) {        
    }

    @Override
    public void onGetFullRefereshSymbol(String symbolName) {        
    }

    @Override
    public void onSeletedSymbolList(List<String> symbol_list) {
    }

    @Override
    public void onSeletedSymbolInfoList(List<SymbolInfo> symbol_info_list) {
    }

}
