package es.ramondin.compdec.mosaico.view.beans;

import es.ramondin.compdec.mosaico.view.util.CeldaMosaico;

import es.ramondin.utilidades.JSFUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;

import java.util.List;

import java.util.Map;

import javax.faces.component.UIComponent;

import oracle.adf.view.rich.component.rich.layout.RichGridCell;
import oracle.adf.view.rich.component.rich.layout.RichGridRow;
import oracle.adf.view.rich.component.rich.layout.RichPanelGridLayout;

public class MosaicoBean {
    private RichPanelGridLayout pgLayout;

    public MosaicoBean() {
    }

    /**
     * Método que inicializa los datos del componente
     */
    private void inicializarComponente() {
        Integer numFilas = (Integer)JSFUtils.resolveExpression("#{attrs.numFilas}");
        Integer numColumnas = (Integer)JSFUtils.resolveExpression("#{attrs.numColumnas}");
        UIComponent[] arrayComponentes = (UIComponent[])JSFUtils.resolveExpression("#{attrs.arrayComponentes}");
        Integer[][] arrayPosiciones = (Integer[][])JSFUtils.resolveExpression("#{attrs.arrayPosiciones}");
        String[] arrayHAlignComps = (String[])JSFUtils.resolveExpression("#{attrs.arrayHAlignComps}");
        String[] arrayVAlignComps = (String[])JSFUtils.resolveExpression("#{attrs.arrayVAlignComps}");
        Integer[] arrayColSpanComps = (Integer[])JSFUtils.resolveExpression("#{attrs.arrayColSpanComps}");
        Integer[] arrayRowSpanComps = (Integer[])JSFUtils.resolveExpression("#{attrs.arrayRowSpanComps}");
        
        int lengthComps = arrayComponentes != null ? arrayComponentes.length : 0;
        int lengthPos = arrayPosiciones != null ? arrayPosiciones.length : 0;
        int lengthHAlign = arrayHAlignComps != null ? arrayHAlignComps.length : 0;
        int lengthVAlign = arrayVAlignComps != null ? arrayVAlignComps.length : 0;
        int lengthColSpan = arrayColSpanComps != null ? arrayColSpanComps.length : 0;
        int lengthRowSpan = arrayRowSpanComps != null ? arrayRowSpanComps.length : 0;

        //Si las posiciones no vienes dadas, completamos en orden por filas, columnas
        if (arrayPosiciones == null) {
            arrayPosiciones = new Integer[lengthComps][2];

            for (int i = 0; i < lengthComps; i++) {
                //No añadimos más filas que el máximo si se ha establecido
                if (numFilas != null && i / numColumnas == numFilas)
                    break;

                arrayPosiciones[i] = new Integer[] { i / numColumnas, i % numColumnas };
            }
        }

        LinkedHashMap<Integer, LinkedHashMap<Integer, CeldaMosaico>> mapaComponentes = new LinkedHashMap<Integer, LinkedHashMap<Integer, CeldaMosaico>>();
        LinkedHashMap<Integer, CeldaMosaico> mapaFila = null;

        int posFila, posCelda;
        String hAlign, vAlign;
        Integer colSpan, rowSpan;
        int maxComponente = Math.min(lengthComps, lengthPos);
        for (int i = 0; i < maxComponente; i++) {
            posFila = arrayPosiciones[i][0];
            posCelda = arrayPosiciones[i][1];

            mapaFila = mapaComponentes.get(posFila);
            if (mapaFila == null) {
                mapaFila = new LinkedHashMap<Integer, CeldaMosaico>();
            }

            hAlign = lengthHAlign > i ? arrayHAlignComps[i] : null;
            vAlign = lengthVAlign > i ? arrayVAlignComps[i] : null;
            colSpan = lengthColSpan > i ? arrayColSpanComps[i] : null;
            rowSpan = lengthRowSpan > i ? arrayRowSpanComps[i] : null;

            mapaFila.put(posCelda, new CeldaMosaico(arrayComponentes[i], hAlign, vAlign, colSpan, rowSpan));
            mapaComponentes.put(posFila, mapaFila);
        }

        List<UIComponent> filas = this.pgLayout.getChildren();
        filas.clear();

        RichGridRow rgRow = null;
        List<UIComponent> celdas = null;
        RichGridCell rgCell = null;

        Iterator it = mapaComponentes.entrySet().iterator();
        Map.Entry<Integer, LinkedHashMap<Integer, CeldaMosaico>> entry = null;
        Iterator itFila = null;
        Map.Entry<Integer, CeldaMosaico> entryFila = null;
        CeldaMosaico celdaMosaico = null;

        while (it.hasNext()) {
            rgRow = new RichGridRow();
            celdas = rgRow.getChildren();

            entry = (Map.Entry<Integer, LinkedHashMap<Integer, CeldaMosaico>>)it.next();

            itFila = entry.getValue().entrySet().iterator();

            while (itFila.hasNext()) {
                rgCell = new RichGridCell();

                entryFila = (Map.Entry<Integer, CeldaMosaico>)itFila.next();
                celdaMosaico = entryFila.getValue();

                rgCell.getChildren().add(celdaMosaico.getComponente());
                if (celdaMosaico.getHAlign() != null)
                    rgCell.setHalign(celdaMosaico.getHAlign());
                if (celdaMosaico.getVAlign() != null)
                    rgCell.setValign(celdaMosaico.getVAlign());
                if (celdaMosaico.getColSpan() != null)
                    rgCell.setColumnSpan(celdaMosaico.getColSpan());

                celdas.add(rgCell);
            }

            filas.add(rgRow);
        }
    }

    public void setPgLayout(RichPanelGridLayout pgLayout) {
        this.pgLayout = pgLayout;

        if (pgLayout != null && pgLayout.getId() != null) {
            inicializarComponente();
        }
    }

    public RichPanelGridLayout getPgLayout() {
        return pgLayout;
    }
}
