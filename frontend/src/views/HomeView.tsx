import { useRef } from 'react'
import { Tile } from '../components/Tile'
import { useRuleset } from '../context/RulesetContext'
import './HomeView.css'

const EMPTY_RULESET_XML = `<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ruleset>
    <attributeSet/>
    <skills/>
</ruleset>`

const staticTiles = [
  {
    id: 'new-ruleset',
    name: 'Neues Regelwerk',
    description: 'Ein neues Regelwerk erstellen',
  },
  {
    id: 'load-ruleset',
    name: 'Regelwerk laden',
    description: 'Ein bestehendes Regelwerk laden',
  },
]

export function HomeView() {
  const fileInputRef = useRef<HTMLInputElement>(null)
  const { currentRuleset, setCurrentRuleset, setFileHandle } = useRuleset()

  function handleFileChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0]
    if (file) {
      setCurrentRuleset(file.name)
    }
    e.target.value = ''
  }

  async function handleNewRuleset() {
    if ('showSaveFilePicker' in window) {
      try {
        const handle = await window.showSaveFilePicker({
          suggestedName: 'regelwerk.xml',
          types: [{ description: 'XML-Regelwerk', accept: { 'application/xml': ['.xml'] } }],
        })
        const writable = await handle.createWritable()
        await writable.write(EMPTY_RULESET_XML)
        await writable.close()
        setFileHandle(handle)
        setCurrentRuleset(handle.name)
      } catch (err) {
        if (err instanceof DOMException && err.name === 'AbortError') return
        const msg = err instanceof Error ? err.message : String(err)
        alert(`Fehler beim Erstellen des Regelwerks: ${msg}`)
      }
    } else {
      const input = window.prompt('Name des neuen Regelwerks:', 'regelwerk')
      if (!input) return
      const filename = input.endsWith('.xml') ? input : `${input}.xml`
      const blob = new Blob([EMPTY_RULESET_XML], { type: 'application/xml' })
      const url = URL.createObjectURL(blob)
      const a = document.createElement('a')
      a.href = url
      a.download = filename
      a.click()
      URL.revokeObjectURL(url)
      setCurrentRuleset(filename)
    }
  }

  const rulesetDisplayName = currentRuleset?.replace(/\.xml$/i, '') ?? ''

  const tiles = currentRuleset
    ? [
        ...staticTiles,
        {
          id: 'edit-ruleset',
          name: `Regelwerk ${rulesetDisplayName} bearbeiten`,
          description: 'Bearbeite das aktuell geladene Regelwerk',
        },
      ]
    : staticTiles

  return (
    <div className="home-view">
      <h1>Rules Engine</h1>
      <input
        ref={fileInputRef}
        type="file"
        accept=".xml"
        style={{ display: 'none' }}
        onChange={handleFileChange}
      />
      <div className="tile-grid">
        {tiles.map((tile) => {
          if (tile.id === 'new-ruleset') {
            return <Tile key={tile.id} {...tile} onClick={handleNewRuleset} />
          }
          if (tile.id === 'load-ruleset') {
            return <Tile key={tile.id} {...tile} onClick={() => fileInputRef.current?.click()} />
          }
          return <Tile key={tile.id} {...tile} />
        })}
      </div>
    </div>
  )
}
