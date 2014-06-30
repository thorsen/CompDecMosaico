package es.ramondin.compdec.mosaico.view.component;

import es.ramondin.compdec.mosaico.view.util.CeldaMosaico;
import es.ramondin.util.general.RmdCompDec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.component.UIComponent;

import oracle.adf.view.rich.component.rich.fragment.RichDeclarativeComponent;
import oracle.adf.view.rich.component.rich.layout.RichGridCell;
import oracle.adf.view.rich.component.rich.layout.RichGridRow;
import oracle.adf.view.rich.component.rich.layout.RichPanelGridLayout;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;


public class CompDecMosaico extends RichDeclarativeComponent {
    private static final String ID_PGLAYOUT = "dc_pgl1";

    public CompDecMosaico() {
    }

    /**
     * Método que refresca los datos del componente
     */
    public void refrescarComponente() {
        Integer numFilas = getNumFilas();
        Integer numColumnas = getNumColumnas();
        UIComponent[] arrayComponentes = getCastedArrayComponentes();
        Integer[][] arrayPosiciones = getCastedArrayPosiciones();
        String[] arrayHAlignComps = getCastedArrayHAlignComps();
        String[] arrayVAlignComps = getCastedArrayVAlignComps();
        Integer[] arrayColSpanComps = getCastedArrayColSpanComps();
        Integer[] arrayRowSpanComps = getCastedArrayRowSpanComps();

        int lengthComps = arrayComponentes != null ? arrayComponentes.length : 0;
        int lengthPos = arrayPosiciones != null ? arrayPosiciones.length : 0;
        int lengthHAlign = arrayHAlignComps != null ? arrayHAlignComps.length : 0;
        int lengthVAlign = arrayVAlignComps != null ? arrayVAlignComps.length : 0;
        int lengthColSpan = arrayColSpanComps != null ? arrayColSpanComps.length : 0;
        int lengthRowSpan = arrayRowSpanComps != null ? arrayRowSpanComps.length : 0;

        boolean arrayCompOrdenado = false;

        //Si las posiciones no vienen dadas, completamos en orden por filas, columnas
        if (arrayPosiciones == null) {
            arrayPosiciones = new Integer[lengthComps][2];

            for (int i = 0; i < lengthComps; i++) {
                //No añadimos más filas que el máximo si se ha establecido
                if (numFilas != null && i / numColumnas == numFilas)
                    break;

                arrayPosiciones[i] = new Integer[] { i / numColumnas, i % numColumnas };
            }

            lengthPos = arrayPosiciones.length;
            arrayCompOrdenado = true;
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

        //Nos quedamos solo con las posiciones relativas a los componentes a representar
        Integer[][] arrayPosicionesAux = new Integer[maxComponente][2];

        for (int i = 0; i < maxComponente; i++) {
            arrayPosicionesAux[i] = new Integer[] { arrayPosiciones[i][0], arrayPosiciones[i][1] };
        }

        if (!arrayCompOrdenado) {
            //Ordenamos el array de posiciones por fila, columna
            Arrays.sort(arrayPosicionesAux, new Comparator<Integer[]>() {
                    @Override
                    public int compare(Integer[] o1, Integer[] o2) {
                        int res = o1[0].compareTo(o2[0]);

                        if (res == 0)
                            res = o1[1].compareTo(o2[1]);

                        return res;
                    }
                });
        }

        RichPanelGridLayout pgLayout = (RichPanelGridLayout)RmdCompDec.getUIComponent(ID_PGLAYOUT, this.getChildren());

        if (pgLayout != null) {
            List<UIComponent> filas = pgLayout.getChildren();
            filas.clear();
            
            List<UIComponent> filasAux = new ArrayList<UIComponent>();

            AdfFacesContext.getCurrentInstance().addPartialTarget(pgLayout);

            RichGridRow rgRow = null;
            List<UIComponent> celdas = null;
            RichGridCell rgCell = null;
            CeldaMosaico celdaMosaico = null;
            /*
        Iterator it = mapaComponentes.entrySet().iterator();
        Map.Entry<Integer, LinkedHashMap<Integer, CeldaMosaico>> entry = null;
        Iterator itFila = null;
        Map.Entry<Integer, CeldaMosaico> entryFila = null;

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
    */
            
            int posFilaAnt = -1, posFilaAct, posCeldaAct, posCeldaAnt = -1;
            int maxCol = 0, maxColAux = 0;
            for (int i = 0; i < maxComponente; i++) {
                posFilaAct = arrayPosicionesAux[i][0];
                posCeldaAct = arrayPosicionesAux[i][1];

                if (posFilaAct != posFilaAnt) {
                    if (posFilaAnt != -1)
                        filasAux.add(rgRow);

                    rgRow = new RichGridRow();
                    celdas = rgRow.getChildren();

                    posFilaAnt = posFilaAct;
                    posCeldaAnt = -1;
                }

                for (; posCeldaAnt < (posCeldaAct - 1); posCeldaAnt++)
                    celdas.add(new RichGridCell());

                rgCell = new RichGridCell();
                celdaMosaico = mapaComponentes.get(posFilaAct).get(posCeldaAct);

                rgCell.getChildren().add(celdaMosaico.getComponente());
                if (celdaMosaico.getHAlign() != null)
                    rgCell.setHalign(celdaMosaico.getHAlign());
                if (celdaMosaico.getVAlign() != null)
                    rgCell.setValign(celdaMosaico.getVAlign());
                if (celdaMosaico.getColSpan() != null)
                    rgCell.setColumnSpan(celdaMosaico.getColSpan());
                if (celdaMosaico.getRowSpan() != null)
                    rgCell.setRowSpan(celdaMosaico.getRowSpan());

                celdas.add(rgCell);
                
                maxColAux = posCeldaAct + (celdaMosaico.getColSpan() != null ? celdaMosaico.getColSpan() : 0);
                
                if (maxColAux > maxCol)
                    maxCol = maxColAux;
                
                posCeldaAnt = posCeldaAct;
            }

            if (maxComponente != 0)
                filasAux.add(rgRow);
            
            //Añadimos la cabecera
            if (getTituloPanel() != null) {
                rgRow = new RichGridRow();
                rgCell = new RichGridCell();
                
                RichOutputText rotTitulo = new RichOutputText();
                rotTitulo.setValue(getTituloPanel());
                rotTitulo.setInlineStyle("font-size:large; font-weight:bold;");
                
                rgCell.getChildren().add(rotTitulo);
                rgCell.setColumnSpan(maxCol);
                rgCell.setHalign(RichGridCell.HALIGN_CENTER);
                
                rgRow.getChildren().add(rgCell);
                filas.add(rgRow);    
            }
            
            //Añadimos el resto de filas
            filas.addAll(filasAux);
            
            AdfFacesContext.getCurrentInstance().addPartialTarget(pgLayout);
        }
    }

    public String getTituloPanel() {
        return (String)getAttributes().get("tituloPanel");
    }

    public Integer getNumFilas() {
        return (Integer)getAttributes().get("numFilas");
    }

    public Integer getNumColumnas() {
        return (Integer)getAttributes().get("numColumnas");
    }

    public Integer getAltoFila() {
        return (Integer)getAttributes().get("altoFila");
    }

    public Integer getAnchoColumna() {
        return (Integer)getAttributes().get("anchoColumna");
    }

    public String getInLineStyleComp() {
        return (String)getAttributes().get("inLineStyleComp");
    }

    public String getDimensionsFromComp() {
        return (String)getAttributes().get("dimensionsFromComp");
    }

    public oracle.jbo.domain.Array getArrayComponentes() {
        return (oracle.jbo.domain.Array)getAttributes().get("arrayComponentes");
    }

    public oracle.jbo.domain.Array getArrayPosiciones() {
        return (oracle.jbo.domain.Array)getAttributes().get("arrayPosiciones");
    }

    public oracle.jbo.domain.Array getArrayHAlignComps() {
        return (oracle.jbo.domain.Array)getAttributes().get("arrayHAlignComps");
    }

    public oracle.jbo.domain.Array getArrayVAlignComps() {
        return (oracle.jbo.domain.Array)getAttributes().get("arrayVAlignComps");
    }

    public oracle.jbo.domain.Array getArrayColSpanComps() {
        return (oracle.jbo.domain.Array)getAttributes().get("arrayColSpanComps");
    }

    public oracle.jbo.domain.Array getArrayRowSpanComps() {
        return (oracle.jbo.domain.Array)getAttributes().get("arrayRowSpanComps");
    }
    
    public UIComponent[] getCastedArrayComponentes() {
        return (UIComponent[])getAttributes().get("arrayComponentes");
    }

    public Integer[][] getCastedArrayPosiciones() {
        return (Integer[][])getAttributes().get("arrayPosiciones");
    }

    public String[] getCastedArrayHAlignComps() {
        return (String[])getAttributes().get("arrayHAlignComps");
    }

    public String[] getCastedArrayVAlignComps() {
        return (String[])getAttributes().get("arrayVAlignComps");
    }

    public Integer[] getCastedArrayColSpanComps() {
        return (Integer[])getAttributes().get("arrayColSpanComps");
    }

    public Integer[] getCastedArrayRowSpanComps() {
        return (Integer[])getAttributes().get("arrayRowSpanComps");
    }
}
