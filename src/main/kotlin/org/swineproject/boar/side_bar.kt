package org.swineproject.boar

import org.eclipse.swt.SWT
import org.eclipse.swt.events.ModifyEvent
import org.eclipse.swt.events.ModifyListener
import org.eclipse.swt.internal.win32.OS
import org.eclipse.swt.internal.win32.TCHAR
import org.eclipse.swt.layout.GridData
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets.*

class SideBar(parent: Composite, val boarWidget: BoarWidget) {
    // lateinit var boarWidget: BoarWidget

    private var group = Group(parent, SWT.NULL)
    private var spinnerX: Spinner
    private var spinnerY: Spinner

    private var nodePosition: Array<Int>? = null

    init {
        val groupLayout = GridLayout()
        groupLayout.numColumns = 5
        group.layout = groupLayout
        group.layoutData = GridData(GridData.FILL_BOTH)

        Label(group, SWT.NONE).text = "Position:"
        Label(group, SWT.NONE).text = "X"
        spinnerX = Spinner(group, SWT.NONE)
        spinnerX.digits = 0
        spinnerX.minimum = -boarWidget.size
        spinnerX.maximum = boarWidget.size
        Label(group, SWT.NONE).text = "Y"
        spinnerY = Spinner(group, SWT.NONE)
        spinnerY.digits = 0

        spinnerX.addModifyListener(object : ModifyListener {
            override fun modifyText(event: ModifyEvent) {
                if (boarWidget.selectedNode != null) {
                    if (spinnerX.text != (boarWidget.selectedNode!!.x - (boarWidget.clientArea.width / 2)).toString()) {
                        boarWidget.selectedNode!!.x = spinnerX.text.toInt() + (boarWidget.clientArea.width / 2)
                    }
                }
            }
        })

        spinnerY.addModifyListener(object : ModifyListener {
            override fun modifyText(event: ModifyEvent) {
                if (boarWidget.selectedNode != null) {
                    if (spinnerY.text != (boarWidget.selectedNode!!.y - (boarWidget.clientArea.height / 2)).toString()) {
                        boarWidget.selectedNode!!.y = spinnerY.text.toInt() + (boarWidget.clientArea.height / 2)
                    }
                }
            }
        })
    }

    fun showSideBar(show: Boolean) {
        group.visible = show
    }

    fun update() {
        // val xField = Spinner::class.java.getDeclaredField("text")
        // xField.isAccessible = true
        // xField.set(spinnerX, boarWidget.selectedNode!!.x.toString())

        // val yField = Spinner::class.java.getField("text")
        // yField.isAccessible = true

        if (boarWidget.selectedNode != null) {
            nodePosition = Util.centerPosition(boarWidget.selectedNode!!.x, boarWidget.selectedNode!!.y, boarWidget.clientArea.width, boarWidget.clientArea.height)

            if (!spinnerX.isFocusControl) {
                spinnerX.setText(nodePosition!![0].toString())
            }

            if (!spinnerY.isFocusControl) {
                spinnerY.setText(nodePosition!![1].toString())
            }
        }
    }

    fun Spinner.setText(value: String) {
        val checkWidget = Widget::class.java.getDeclaredMethod("checkWidget")
        checkWidget.isAccessible = true
        val getCodePage = Control::class.java.getDeclaredMethod("getCodePage")
        getCodePage.isAccessible = true

        val hwndText = Spinner::class.java.getDeclaredField("hwndText")
        hwndText.isAccessible = true

        checkWidget.invoke(this)
        val buffer = TCHAR(getCodePage.invoke(this).toString().toInt(), value, true)

        OS.SetWindowText(hwndText.getLong(this), buffer)
    }
}