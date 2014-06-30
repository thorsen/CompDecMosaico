package es.ramondin.compdec.mosaico.view.beans;

import oracle.adf.view.rich.component.rich.layout.RichPanelGridLayout;


public class MosaicoBean {
    private RichPanelGridLayout pgLayout;

    public MosaicoBean() {
    }

    public void setPgLayout(RichPanelGridLayout pgLayout) {
        this.pgLayout = pgLayout;
    }

    public RichPanelGridLayout getPgLayout() {
        return pgLayout;
    }
}
