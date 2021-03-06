package org.swineproject.boar

import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import org.eclipse.swt.layout.GridLayout


fun main(args: Array<String>) {
    val display = Display()
    val shell = Shell(display)
    shell.text = "Boar"
    shell.setMinimumSize(800, 740)

    val layout = GridLayout()
    layout.numColumns = 2
    shell.layout = layout

    val boarWidget = BoarWidget(display, shell)

    val sideBar = SideBar(shell, boarWidget)
    boarWidget.sideBar = sideBar

    val timelineWidget = TimelineWidget(shell, boarWidget)
    val gridLayout = GridLayout()
    gridLayout.numColumns = 1
    timelineWidget.layout = gridLayout
    boarWidget.timelineWidget = timelineWidget

    boarWidget.setFocus()

    shell.pack()
    shell.open()

    val delay = 24

    val timer = object : Runnable {
        override fun run() {
            if (!boarWidget.isDisposed) {
                boarWidget.redraw()
            }

            if (!sideBar.nodeGroup.isDisposed) {
                sideBar.update()
            }

            display.timerExec(delay, this)
        }
    }

    display.timerExec(delay, timer)

    while (!shell.isDisposed) {
        if (!display.readAndDispatch()) {
            display.sleep()
        }
    }
    display.dispose()
}