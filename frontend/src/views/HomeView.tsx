import { useRef } from 'react'
import { Tile } from '../components/Tile'
import { useRuleset } from '../context/RulesetContext'
import './HomeView.css'

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
  const { currentRuleset, setCurrentRuleset } = useRuleset()

  function handleFileChange(e: React.ChangeEvent<HTMLInputElement>) {
    const file = e.target.files?.[0]
    if (file) {
      setCurrentRuleset(file.name)
    }
    e.target.value = ''
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
        {tiles.map((tile) =>
          tile.id === 'load-ruleset' ? (
            <Tile key={tile.id} {...tile} onClick={() => fileInputRef.current?.click()} />
          ) : (
            <Tile key={tile.id} {...tile} />
          )
        )}
      </div>
    </div>
  )
}
