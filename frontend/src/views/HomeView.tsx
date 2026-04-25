import { Tile } from '../components/Tile'
import './HomeView.css'

const tiles = [
  {
    id: 'new-ruleset',
    name: 'Neues Regelwerk',
    description: 'Ein neues Regelwerk erstellen',
  },
  {
    id: 'load-ruleset',
    name: 'Regelwerk laden',
    description: 'ein bestehendes Regelwerk laden',
  },
]

export function HomeView() {
  return (
    <div className="home-view">
      <h1>Rules Engine</h1>
      <div className="tile-grid">
        {tiles.map((tile) => (
          <Tile key={tile.id} {...tile} />
        ))}
      </div>
    </div>
  )
}
