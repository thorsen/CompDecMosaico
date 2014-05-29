package es.ramondin.compdec.mosaico.view.util;

import javax.faces.component.UIComponent;

public class CeldaMosaico {
    private UIComponent componente;
    private String hAlign;
    private String vAlign;
    private Integer colSpan;
    private Integer rowSpan;

    public CeldaMosaico() {
        super();
    }

    public CeldaMosaico(UIComponent componente, String hAlign, String vAlign, Integer colSpan, Integer rowSpan) {
        this();

        this.componente = componente;
        this.hAlign = hAlign;
        this.vAlign = vAlign;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public void setComponente(UIComponent componente) {
        this.componente = componente;
    }

    public UIComponent getComponente() {
        return componente;
    }

    public void setHAlign(String hAlign) {
        this.hAlign = hAlign;
    }

    public String getHAlign() {
        return hAlign;
    }

    public void setVAlign(String vAlign) {
        this.vAlign = vAlign;
    }

    public String getVAlign() {
        return vAlign;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }
}
