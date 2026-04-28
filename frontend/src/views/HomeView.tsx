import { useNavigate } from 'react-router-dom'
import { Tile } from '../components/Tile'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, importRuleset } from '../api'
import type { RulesetData } from '../api'
import { useState } from 'react'
import './HomeView.css'

const XML_PICKER_OPTS: OpenFilePickerOptions = {
  types: [{ description: 'XML-Regelwerk', accept: { 'text/xml': ['.xml'] } }],
  multiple: false,
}

const XML_SAVE_OPTS: SaveFilePickerOptions = {
  suggestedName: 'regelwerk.xml',
  types: [{ description: 'XML-Regelwerk', accept: { 'text/xml': ['.xml'] } }],
}

export function HomeView() {
  const navigate = useNavigate()
  const { currentRuleset, setCurrentRuleset, setRulesetData, setFileHandle } = useRuleset()
  const [error, setError] = useState<string | null>(null)

  async function handleLoad() {
    setError(null)
    if (window.showOpenFilePicker) {
      try {
        const [handle] = await window.showOpenFilePicker(XML_PICKER_OPTS)
        const file = await handle.getFile()
        const xml = await file.text()
        const data = await importRuleset(xml)
        setFileHandle(handle)
        setCurrentRuleset(handle.name.replace(/\.xml$/i, ''))
        setRulesetData(data)
        navigate('/edit-ruleset')
      } catch (e) {
        if (e instanceof DOMException && e.name === 'AbortError') return
        setError(e instanceof Error ? e.message : String(e))
      }
    } else {
      const input = document.createElement('input')
      input.type = 'file'
      input.accept = '.xml,text/xml'
      input.onchange = async () => {
        const file = input.files?.[0]
        if (!file) return
        try {
          const xml = await file.text()
          const data = await importRuleset(xml)
          setFileHandle(null)
          setCurrentRuleset(file.name.replace(/\.xml$/i, ''))
          setRulesetData(data)
          navigate('/edit-ruleset')
        } catch (e) {
          setError(e instanceof Error ? e.message : String(e))
        }
      }
      input.click()
    }
  }

  async function handleNew() {
    setError(null)
    if (window.showSaveFilePicker) {
      try {
        const handle = await window.showSaveFilePicker(XML_SAVE_OPTS)
        const emptyData: RulesetData = {
          valueRange: { min: -10, average: 0, max: 10 },
          attributes: [],
          skills: [],
          skillDomains: [],
        }
        const xml = await exportRuleset(emptyData)
        const writable = await handle.createWritable()
        await writable.write(xml)
        await writable.close()
        setFileHandle(handle)
        setCurrentRuleset(handle.name.replace(/\.xml$/i, ''))
        setRulesetData(emptyData)
        navigate('/edit-ruleset')
      } catch (e) {
        if (e instanceof DOMException && e.name === 'AbortError') return
        setError(e instanceof Error ? e.message : String(e))
      }
    } else {
      const name = window.prompt('Name des Regelwerks:', 'Neues Regelwerk')
      if (!name) return
      const emptyData: RulesetData = {
        valueRange: { min: -10, average: 0, max: 10 },
        attributes: [],
        skills: [],
        skillDomains: [],
      }
      setFileHandle(null)
      setCurrentRuleset(name)
      setRulesetData(emptyData)
      navigate('/edit-ruleset')
    }
  }

  return (
    <div className="home-view">
      <h1>Rules Engine</h1>
      {error && <p className="home-error">{error}</p>}
      <div className="tile-grid">
        <Tile
          id="new-ruleset"
          name="Neue Referenzregeln"
          description="Neue Referenzregeln erstellen"
          onClick={handleNew}
        />
        <Tile
          id="load-ruleset"
          name="Referenzregeln laden"
          description="Referenzregeln laden"
          onClick={handleLoad}
        />
        {currentRuleset && (
          <Tile
            id="edit-ruleset"
            name={`Referenzregeln ${currentRuleset} bearbeiten`}
            description={`Die Referenzregeln ${currentRuleset} bearbeiten.`}
            onClick={() => navigate('/edit-ruleset')}
          />
        )}
        {currentRuleset && (
          <Tile
            id="create-world"
            name="Spielwelt anlegen"
            description={`Eine neue Spielwelt auf Basis der Referenzregeln ${currentRuleset} erstellen.`}
            onClick={() => navigate('/create-world')}
          />
        )}

      </div>
    </div>
  )
}
